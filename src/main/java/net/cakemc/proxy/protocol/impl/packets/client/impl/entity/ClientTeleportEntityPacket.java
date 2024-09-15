package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Position;

public class ClientTeleportEntityPacket extends AbstractPacket {

	private int entityId;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private boolean onGround;

	public ClientTeleportEntityPacket(int entityId, Position position, boolean onGround) {
		this.entityId = entityId;
		this.x = position.getX();
		this.y = position.getY();
		this.z = position.getZ();
		this.yaw = position.getYaw();
		this.pitch = position.getPitch();
		this.onGround = onGround;
	}

	public ClientTeleportEntityPacket(int entityId, double x, double y, double z, float yaw, float pitch, boolean onGround) {
		this.entityId = entityId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	public ClientTeleportEntityPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.yaw = buf.readByte() * 360 / 256f;
		this.pitch = buf.readByte() * 360 / 256f;
		this.onGround = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		buf.writeDouble(this.x);
		buf.writeDouble(this.y);
		buf.writeDouble(this.z);
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

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
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

