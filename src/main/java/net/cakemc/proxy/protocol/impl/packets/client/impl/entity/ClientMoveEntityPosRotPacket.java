package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Position;

public class ClientMoveEntityPosRotPacket extends AbstractPacket {

	private int entityId;
	private double moveX;
	private double moveY;
	private double moveZ;
	private float yaw;
	private float pitch;
	private boolean onGround;

	public ClientMoveEntityPosRotPacket(int entityId, Position position, boolean onGround) {
		this(entityId, position.getX(), position.getY(), position.getZ(), position.getYaw(), position.getPitch(), onGround);
	}

	public ClientMoveEntityPosRotPacket(int entityId, double moveX, double moveY, double moveZ, float yaw, float pitch, boolean onGround) {
		this.entityId = entityId;
		this.moveX = moveX;
		this.moveY = moveY;
		this.moveZ = moveZ;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	public ClientMoveEntityPosRotPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.moveX = buf.readShort() / 4096D;
		this.moveY = buf.readShort() / 4096D;
		this.moveZ = buf.readShort() / 4096D;
		this.yaw = buf.readByte() * 360 / 256f;
		this.pitch = buf.readByte() * 360 / 256f;
		this.onGround = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		buf.writeShort((int) this.moveX);
		buf.writeShort((int) this.moveY);
		buf.writeShort((int) this.moveZ);
		writeAngel(buf, this.yaw);
		writeAngel(buf, this.pitch);
		//buf.writeShort((int) (this.moveX * 4096));
		//buf.writeShort((int) (this.moveY * 4096));
		//buf.writeShort((int) (this.moveZ * 4096));
		//buf.writeByte((byte) (this.yaw * 256 / 360));
		//buf.writeByte((byte) (this.pitch * 256 / 360));
		buf.writeBoolean(this.onGround);

	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public double getMoveX() {
		return moveX;
	}

	public void setMoveX(double moveX) {
		this.moveX = moveX;
	}

	public double getMoveY() {
		return moveY;
	}

	public void setMoveY(double moveY) {
		this.moveY = moveY;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public double getMoveZ() {
		return moveZ;
	}

	public void setMoveZ(double moveZ) {
		this.moveZ = moveZ;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
}

