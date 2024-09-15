package net.cakemc.proxy.protocol.impl.packets.server.impl.level;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerAcceptTeleportationPacket extends AbstractPacket {

	private int teleportId;

	public ServerAcceptTeleportationPacket(int teleportId) {
		this.teleportId = teleportId;
	}

	public ServerAcceptTeleportationPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		teleportId = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, teleportId);
	}

	public int getTeleportId() {
		return teleportId;
	}

	public void setTeleportId(int teleportId) {
		this.teleportId = teleportId;
	}
}

