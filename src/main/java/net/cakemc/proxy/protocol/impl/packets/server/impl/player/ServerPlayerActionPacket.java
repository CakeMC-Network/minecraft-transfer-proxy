package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.PlayerAction;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Direction;
import net.cakemc.mc.lib.world.WorldPosition;

public class ServerPlayerActionPacket extends AbstractPacket {

	private PlayerAction action;
	private WorldPosition position;
	private Direction face;
	private int sequence;

	public ServerPlayerActionPacket(PlayerAction action, WorldPosition position, Direction face, int sequence) {
		this.action = action;
		this.position = position;
		this.face = face;
		this.sequence = sequence;
	}

	public ServerPlayerActionPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.action = PlayerAction.values()[readVarInt(buf)];
		this.position = readPosition(buf);
		this.face = Direction.values()[readVarInt(buf)];
		this.sequence = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.action.ordinal());
		writePosition(buf, this.position);
		writeVarInt(buf, face.ordinal());
		writeVarInt(buf, this.sequence);
	}

	@Override public String toString() {
		return "ServerPlayerActionPacket{" +
		       "action=" + action +
		       ", position=" + position +
		       ", face=" + face +
		       ", sequence=" + sequence +
		       '}';
	}

	public PlayerAction getAction() {
		return action;
	}

	public void setAction(PlayerAction action) {
		this.action = action;
	}

	public WorldPosition getPosition() {
		return position;
	}

	public void setPosition(WorldPosition position) {
		this.position = position;
	}

	public Direction getFace() {
		return face;
	}

	public void setFace(Direction face) {
		this.face = face;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}

