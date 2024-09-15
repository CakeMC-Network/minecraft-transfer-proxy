package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientIntentionPacket extends AbstractPacket {

	private int protocolVersion;
	private String host;
	private int port;
	private int state;

	public ClientIntentionPacket(int protocolVersion, String host, int port, int state) {
		this.protocolVersion = protocolVersion;
		this.host = host;
		this.port = port;
		this.state = state;
	}

	public ClientIntentionPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		protocolVersion = readVarInt(buf);
		host = readString(buf);
		port = buf.readUnsignedShort();
		state = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, protocolVersion);
		writeString(buf, host);
		buf.writeShort(port);
		writeVarInt(buf, state);
	}

	@Override
	public String toString() {
		return "ClientIntentionPacket{" +
		       "protocolVersion=" + protocolVersion +
		       ", host='" + host + '\'' +
		       ", port=" + port +
		       ", state=" + state +
		       '}';
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}

