package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.UUID;

public class ServerHelloPacket extends AbstractPacket {
	private String username;
	private UUID uuid;

	public ServerHelloPacket(String username, UUID uuid) {
		this.username = username;
		this.uuid = uuid;
	}

	public ServerHelloPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.username = readString(buf);
		this.uuid = readUUID(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.username);
		writeUUID(buf, this.uuid);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "ServerHelloPacket{" +
		       "username='" + username + '\'' +
		       ", uuid=" + uuid +
		       '}';
	}
}
