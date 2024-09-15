package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.UUID;

public class ClientResourcePackPopPacket extends AbstractPacket {

	private UUID uuid;

	public ClientResourcePackPopPacket(UUID uuid) {
		this.uuid = uuid;
	}

	public ClientResourcePackPopPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		buf.writeBoolean(uuid != null);
		if (uuid != null) {
			writeUUID(buf, uuid);
		}

	}

	@Override
	public void write(ByteBuf buf) {
		if (buf.readBoolean())
			this.uuid = readUUID(buf);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}

