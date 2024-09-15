package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientMoveEntityRotPacket extends AbstractPacket {
	private int entityId;
	private float yaw;
	private float pitch;
	private boolean onGround;

	public ClientMoveEntityRotPacket(int entityId, float yaw, float pitch, boolean onGround) {
		this.entityId = entityId;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	public ClientMoveEntityRotPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.yaw = buf.readByte() * 360 / 256f;
		this.pitch = buf.readByte() * 360 / 256f;
		this.onGround = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		buf.writeByte((byte) (this.yaw * 256 / 360));
		buf.writeByte((byte) (this.pitch * 256 / 360));
		buf.writeBoolean(this.onGround);
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
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

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
}

