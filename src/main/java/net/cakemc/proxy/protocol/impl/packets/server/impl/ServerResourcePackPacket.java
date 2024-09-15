package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.ResourcePackResponse;
	 import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.UUID;

public class ServerResourcePackPacket extends AbstractPacket {

	private UUID uuid;
	private ResourcePackResponse response;

	public ServerResourcePackPacket(UUID uuid, ResourcePackResponse response) {
		this.uuid = uuid;
		this.response = response;
	}

	public ServerResourcePackPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.uuid = readUUID(buf);
		this.response = ResourcePackResponse.values()[readVarInt(buf)];
	}

	@Override
	public void write(ByteBuf buf) {
		writeUUID(buf, uuid);
		writeVarInt(buf, response.ordinal());
	}

	public UUID getUuid() {
		return uuid;
	}

	public ResourcePackResponse getResponse() {
		return response;
	}
}

