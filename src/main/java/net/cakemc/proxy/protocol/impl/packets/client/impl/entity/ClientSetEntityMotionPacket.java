package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Vector;

public class ClientSetEntityMotionPacket extends AbstractPacket {

	private int entityId;
	private double motionX;
	private double motionY;
	private double motionZ;

	public ClientSetEntityMotionPacket(int entityId, Vector vector) {
		this.entityId = entityId;
		this.motionX = vector.getX();
		this.motionY = vector.getY();
		this.motionZ = vector.getZ();
	}

	public ClientSetEntityMotionPacket(int entityId, double motionX, double motionY, double motionZ) {
		this.entityId = entityId;
		this.motionX = (short) clamp(motionX, Short.MIN_VALUE, Short.MAX_VALUE);
		this.motionY = (short) clamp(motionY, Short.MIN_VALUE, Short.MAX_VALUE);
		this.motionZ = (short) clamp(motionZ, Short.MIN_VALUE, Short.MAX_VALUE);
	}

	public ClientSetEntityMotionPacket() {
	}

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.motionX = buf.readShort();
		this.motionY = buf.readShort();
		this.motionZ = buf.readShort();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		buf.writeShort((int) (this.motionX));
		buf.writeShort((int) (this.motionY));
		buf.writeShort((int) (this.motionZ));
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public double getMotionX() {
		return motionX;
	}

	public void setMotionX(double motionX) {
		this.motionX = motionX;
	}

	public double getMotionY() {
		return motionY;
	}

	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}

	public double getMotionZ() {
		return motionZ;
	}

	public void setMotionZ(double motionZ) {
		this.motionZ = motionZ;
	}

	@Override public String toString() {
		return "ClientSetEntityMotionPacket{" +
		       "entityId=" + entityId +
		       ", motionX=" + motionX +
		       ", motionY=" + motionY +
		       ", motionZ=" + motionZ +
		       '}';
	}

}

