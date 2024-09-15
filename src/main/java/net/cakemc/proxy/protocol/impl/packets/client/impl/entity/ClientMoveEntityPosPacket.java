package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientMoveEntityPosPacket extends AbstractPacket {
	private int entityId;
	private double moveX;
	private double moveY;
	private double moveZ;
	private boolean onGround;

	public ClientMoveEntityPosPacket(int entityId, double moveX, double moveY, double moveZ, boolean onGround) {
		this.entityId = entityId;
		this.moveX = moveX;
		this.moveY = moveY;
		this.moveZ = moveZ;
		this.onGround = onGround;
	}

	public ClientMoveEntityPosPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.moveX = buf.readShort() / 4096D;
		this.moveY = buf.readShort() / 4096D;
		this.moveZ = buf.readShort() / 4096D;
		this.onGround = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		buf.writeShort((int) (this.moveX * 4096));
		buf.writeShort((int) (this.moveY * 4096));
		buf.writeShort((int) (this.moveZ * 4096));
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

