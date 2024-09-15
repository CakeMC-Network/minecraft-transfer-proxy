package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientTransferPacket extends AbstractPacket {

	private String host;
	private int port;

	public ClientTransferPacket(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public ClientTransferPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.host = readString(buf);
		this.port = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, host);
		writeVarInt(buf, port);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
}

