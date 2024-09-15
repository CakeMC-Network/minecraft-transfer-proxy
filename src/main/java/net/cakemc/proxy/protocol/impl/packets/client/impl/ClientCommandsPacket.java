package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.command.completion.CommandNode;
import net.cakemc.mc.lib.game.command.completion.CommandParser;
import net.cakemc.mc.lib.game.command.completion.CommandType;
import net.cakemc.mc.lib.game.command.completion.properties.*;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.OptionalInt;

public class ClientCommandsPacket extends AbstractPacket {

	private static final int FLAG_TYPE_MASK = 0x03;
	private static final int FLAG_EXECUTABLE = 0x04;
	private static final int FLAG_REDIRECT = 0x08;
	private static final int FLAG_SUGGESTION_TYPE = 0x10;

	private static final int NUMBER_FLAG_MIN_DEFINED = 0x01;
	private static final int NUMBER_FLAG_MAX_DEFINED = 0x02;

	private static final int ENTITY_FLAG_SINGLE_TARGET = 0x01;
	private static final int ENTITY_FLAG_PLAYERS_ONLY = 0x02;

	private CommandNode[] nodes;
	private int firstNodeIndex;

	public ClientCommandsPacket(CommandNode[] nodes, int firstNodeIndex) {
		this.nodes = nodes;
		this.firstNodeIndex = firstNodeIndex;
	}

	public ClientCommandsPacket() {
	}

	public CommandNode[] getNodes() {
		return nodes;
	}

	public int getFirstNodeIndex() {
		return firstNodeIndex;
	}

	@Override
	public void read(ByteBuf buf) {
		this.nodes = new CommandNode[readVarInt(buf)];
		for (int i = 0 ; i < this.nodes.length ; i++) {
			byte flags = buf.readByte();
			CommandType type = CommandType.byId(flags & FLAG_TYPE_MASK);
			boolean executable = (flags & FLAG_EXECUTABLE) != 0;

			int[] children = new int[readVarInt(buf)];
			for (int j = 0 ; j < children.length ; j++) {
				children[j] = readVarInt(buf);
			}

			OptionalInt redirectIndex;
			if ((flags & FLAG_REDIRECT) != 0) {
				redirectIndex = OptionalInt.of(readVarInt(buf));
			} else {
				redirectIndex = OptionalInt.empty();
			}

			String name = null;
			if (type == CommandType.LITERAL || type == CommandType.ARGUMENT) {
				name = readString(buf);
			}

			CommandParser parser = null;
			CommandProperties properties = null;
			String suggestionType = null;
			if (type == CommandType.ARGUMENT) {
				parser = CommandParser.byId(readVarInt(buf));
				switch (parser) {
					case DOUBLE -> {
						byte numberFlags = buf.readByte();
						double min = -Double.MAX_VALUE;
						double max = Double.MAX_VALUE;
						if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
							min = buf.readDouble();
						}

						if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
							max = buf.readDouble();
						}

						properties = new DoubleProperties(min, max);
					}
					case FLOAT -> {
						byte numberFlags = buf.readByte();
						float min = -Float.MAX_VALUE;
						float max = Float.MAX_VALUE;
						if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
							min = buf.readFloat();
						}

						if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
							max = buf.readFloat();
						}

						properties = new FloatProperties(min, max);
					}
					case INTEGER -> {
						byte numberFlags = buf.readByte();
						int min = Integer.MIN_VALUE;
						int max = Integer.MAX_VALUE;
						if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
							min = buf.readInt();
						}

						if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
							max = buf.readInt();
						}

						properties = new IntegerProperties(min, max);
					}
					case LONG -> {
						byte numberFlags = buf.readByte();
						long min = Long.MIN_VALUE;
						long max = Long.MAX_VALUE;
						if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
							min = buf.readLong();
						}

						if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
							max = buf.readLong();
						}

						properties = new LongProperties(min, max);
					}
					case STRING -> properties = StringProperties.from(readVarInt(buf));
					case ENTITY -> {
						byte entityFlags = buf.readByte();
						properties = new EntityProperties(
							 (entityFlags & ENTITY_FLAG_SINGLE_TARGET) != 0,
							 (entityFlags & ENTITY_FLAG_PLAYERS_ONLY) != 0
						);
					}
					case SCORE_HOLDER -> properties = new ScoreHolderProperties(buf.readBoolean());
					case TIME -> properties = new TimeProperties(buf.readInt());
					case RESOURCE_OR_TAG, RESOURCE_OR_TAG_KEY, RESOURCE, RESOURCE_KEY -> properties = new ResourceProperties(readString(buf));
					default -> {
					}
				}

				if ((flags & FLAG_SUGGESTION_TYPE) != 0) {
					suggestionType = readString(buf);
				}
			}

			this.nodes[i] = new CommandNode(type, executable, children, redirectIndex, name, parser, properties, suggestionType);
		}

		this.firstNodeIndex = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.nodes.length);
		for (CommandNode node : this.nodes) {
			int flags = node.getType().ordinal() & FLAG_TYPE_MASK;
			if (node.isExecutable()) {
				flags |= FLAG_EXECUTABLE;
			}

			if (node.getRedirectIndex().isPresent()) {
				flags |= FLAG_REDIRECT;
			}

			if (node.getSuggestionType() != null) {
				flags |= FLAG_SUGGESTION_TYPE;
			}

			buf.writeByte(flags);

			writeVarInt(buf, node.getChildIndices().length);
			for (int childIndex : node.getChildIndices()) {
				writeVarInt(buf, childIndex);
			}

			if (node.getRedirectIndex().isPresent()) {
				writeVarInt(buf, node.getRedirectIndex().getAsInt());
			}

			if (node.getType() == CommandType.LITERAL || node.getType() == CommandType.ARGUMENT) {
				writeString(buf, node.getName());
			}

			if (node.getType() == CommandType.ARGUMENT) {
				writeVarInt(buf, node.getParser().ordinal());
				switch (node.getParser()) {
					case DOUBLE -> {
						DoubleProperties properties = (DoubleProperties) node.getProperties();

						int numberFlags = 0;
						if (properties.getMin() != -Double.MAX_VALUE) {
							numberFlags |= NUMBER_FLAG_MIN_DEFINED;
						}

						if (properties.getMax() != Double.MAX_VALUE) {
							numberFlags |= NUMBER_FLAG_MAX_DEFINED;
						}

						buf.writeByte(numberFlags);
						if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
							buf.writeDouble(properties.getMin());
						}

						if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
							buf.writeDouble(properties.getMax());
						}
					}
					case FLOAT -> {
						FloatProperties properties = (FloatProperties) node.getProperties();

						int numberFlags = 0;
						if (properties.getMin() != -Float.MAX_VALUE) {
							numberFlags |= NUMBER_FLAG_MIN_DEFINED;
						}

						if (properties.getMax() != Float.MAX_VALUE) {
							numberFlags |= NUMBER_FLAG_MAX_DEFINED;
						}

						buf.writeByte(numberFlags);
						if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
							buf.writeFloat(properties.getMin());
						}

						if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
							buf.writeFloat(properties.getMax());
						}
					}
					case INTEGER -> {
						IntegerProperties properties = (IntegerProperties) node.getProperties();

						int numberFlags = 0;
						if (properties.getMin() != Integer.MIN_VALUE) {
							numberFlags |= NUMBER_FLAG_MIN_DEFINED;
						}

						if (properties.getMax() != Integer.MAX_VALUE) {
							numberFlags |= NUMBER_FLAG_MAX_DEFINED;
						}

						buf.writeByte(numberFlags);
						if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
							buf.writeInt(properties.getMin());
						}

						if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
							buf.writeInt(properties.getMax());
						}
					}
					case LONG -> {
						LongProperties properties = (LongProperties) node.getProperties();

						int numberFlags = 0;
						if (properties.getMin() != Long.MIN_VALUE) {
							numberFlags |= NUMBER_FLAG_MIN_DEFINED;
						}

						if (properties.getMax() != Long.MAX_VALUE) {
							numberFlags |= NUMBER_FLAG_MAX_DEFINED;
						}

						buf.writeByte(numberFlags);
						if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
							buf.writeLong(properties.getMin());
						}

						if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
							buf.writeLong(properties.getMax());
						}
					}
					case STRING -> writeVarInt(buf, ((StringProperties) node.getProperties()).ordinal());
					case ENTITY -> {
						EntityProperties properties = (EntityProperties) node.getProperties();
						int entityFlags = 0;
						if (properties.isSingleTarget()) {
							entityFlags |= ENTITY_FLAG_SINGLE_TARGET;
						}

						if (properties.isPlayersOnly()) {
							entityFlags |= ENTITY_FLAG_PLAYERS_ONLY;
						}

						buf.writeByte(entityFlags);
					}
					case SCORE_HOLDER -> buf.writeBoolean(((ScoreHolderProperties) node.getProperties()).isAllowMultiple());
					case TIME -> buf.writeInt(((TimeProperties) node.getProperties()).getMin());
					case RESOURCE_OR_TAG, RESOURCE_OR_TAG_KEY, RESOURCE, RESOURCE_KEY -> writeString(buf, ((ResourceProperties) node.getProperties()).getRegistryKey());
					default -> {
					}
				}

				if (node.getSuggestionType() != null) {
					writeString(buf, node.getSuggestionType());
				}
			}
		}

		writeVarInt(buf, this.firstNodeIndex);
	}

}

