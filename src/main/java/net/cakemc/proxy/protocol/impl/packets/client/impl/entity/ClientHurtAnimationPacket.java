package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientHurtAnimationPacket extends AbstractPacket {

	private int entityId;
	private float yaw;

	public ClientHurtAnimationPacket(int entityId, float yaw) {
		this.entityId = entityId;
		this.yaw = yaw;
	}

	public ClientHurtAnimationPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.yaw = buf.readFloat();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, entityId);
		buf.writeFloat(yaw);
	}

	public int getEntityId() {
		return entityId;
	}

	public float getYaw() {
		return yaw;
	}
}

