package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.Hand.Type;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Direction;
import net.cakemc.mc.lib.world.WorldPosition;

public class ServerUseItemOnPacket extends AbstractPacket {

	private WorldPosition position;
	private Direction face;
	private Type hand;
	private float cursorX;
	private float cursorY;
	private float cursorZ;
	private boolean insideBlock;
	private int sequence;

	public ServerUseItemOnPacket(WorldPosition position, Direction face, Type hand, float cursorX, float cursorY, float cursorZ, boolean insideBlock, int sequence) {
		this.position = position;
		this.face = face;
		this.hand = hand;
		this.cursorX = cursorX;
		this.cursorY = cursorY;
		this.cursorZ = cursorZ;
		this.insideBlock = insideBlock;
		this.sequence = sequence;
	}

	public ServerUseItemOnPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.hand = Type.values()[readVarInt(buf)];
		this.position = readPosition(buf);
		this.face = Direction.values()[readVarInt(buf)];
		this.cursorX = buf.readFloat();
		this.cursorY = buf.readFloat();
		this.cursorZ = buf.readFloat();
		this.insideBlock = buf.readBoolean();
		this.sequence = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.hand.ordinal());
		writePosition(buf, this.position);
		writeVarInt(buf, face.ordinal());
		buf.writeFloat(this.cursorX);
		buf.writeFloat(this.cursorY);
		buf.writeFloat(this.cursorZ);
		buf.writeBoolean(this.insideBlock);
		writeVarInt(buf, this.sequence);
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

	public Type getHand() {
		return hand;
	}

	public void setHand(Type hand) {
		this.hand = hand;
	}

	public float getCursorX() {
		return cursorX;
	}

	public void setCursorX(float cursorX) {
		this.cursorX = cursorX;
	}

	public float getCursorY() {
		return cursorY;
	}

	public void setCursorY(float cursorY) {
		this.cursorY = cursorY;
	}

	public float getCursorZ() {
		return cursorZ;
	}

	public void setCursorZ(float cursorZ) {
		this.cursorZ = cursorZ;
	}

	public boolean isInsideBlock() {
		return insideBlock;
	}

	public void setInsideBlock(boolean insideBlock) {
		this.insideBlock = insideBlock;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}

