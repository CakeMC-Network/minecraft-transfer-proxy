package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientRotateHeadPacket extends AbstractPacket {
	private int entityId;
	private float headYaw;

	public ClientRotateHeadPacket(int entityId, float headYaw) {
		this.entityId = entityId;
		this.headYaw = headYaw;
	}

	public ClientRotateHeadPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.headYaw = buf.readByte() * 360 / 256f;
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		buf.writeByte((byte) (this.headYaw * 256 / 360));
	}


	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public float getHeadYaw() {
		return headYaw;
	}

	public void setHeadYaw(float headYaw) {
		this.headYaw = headYaw;
	}
}

