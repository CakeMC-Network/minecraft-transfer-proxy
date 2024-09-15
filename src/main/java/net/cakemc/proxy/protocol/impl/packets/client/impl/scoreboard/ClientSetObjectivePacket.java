package net.cakemc.proxy.protocol.impl.packets.client.impl.scoreboard;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.game.scoreboard.ScoreboardType;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType.Action;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType.Type;
import net.cakemc.mc.lib.game.text.number.NumberFormat;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetObjectivePacket extends AbstractPacket {
	private String name;
	private Action action;

	private BaseComponent displayName;

	private Type type;
	private NumberFormat numberFormat;

	public ClientSetObjectivePacket(String name, Action action, BaseComponent displayName, Type type, NumberFormat numberFormat) {
		this.name = name;
		this.action = action;
		this.displayName = displayName;
		this.type = type;
		this.numberFormat = numberFormat;
	}

	public static ClientSetObjectivePacket create(final String name, final BaseComponent displayName, final NumberFormat numberFormat) {
		return new ClientSetObjectivePacket(name, Action.ADD, displayName, Type.NUMBER, numberFormat);
	}

	public static ClientSetObjectivePacket update(final String name, final BaseComponent displayName, final NumberFormat numberFormat) {
		return new ClientSetObjectivePacket(name, Action.UPDATE, displayName, Type.NUMBER, numberFormat);
	}

	public static ClientSetObjectivePacket remove(final String name) {
		return new ClientSetObjectivePacket(name, Action.REMOVE, null, Type.NUMBER, null);
	}

	@Override
	public void read(ByteBuf buf) {
		this.name = readString(buf);
		this.action = Action.values()[buf.readByte()];
		if (this.action == Action.ADD || this.action == Action.UPDATE) {
			this.displayName = readComponent(buf);
			this.type = Type.values()[readVarInt(buf)];
			if (buf.readBoolean()) {
				this.numberFormat = readNumberFormat(buf);
			}
		} else {
			this.displayName = null;
			this.type = Type.NUMBER;
			this.numberFormat = null;
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.name);
		buf.writeByte(this.action.ordinal());
		if (this.action == Action.ADD || this.action == Action.UPDATE) {
			writeComponent(buf, this.displayName);
			writeVarInt(buf, this.type.ordinal());
			buf.writeBoolean(numberFormat != null);
			if (numberFormat != null) {
				writeNumberFormat(buf, numberFormat);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public BaseComponent getDisplayName() {
		return displayName;
	}

	public void setDisplayName(BaseComponent displayName) {
		this.displayName = displayName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}
}

