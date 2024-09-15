package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerCookieResponsePacket extends AbstractPacket {

	private String key;
	private byte[] data;

	public ServerCookieResponsePacket(String key, byte[] data) {
		this.key = key;
		this.data = data;
	}

	public ServerCookieResponsePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.key = readString(buf);

		if (buf.readBoolean()) {
			this.data = new byte[readVarInt(buf)];
			buf.readBytes(this.data);
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.key);

		buf.writeBoolean(data != null);
		if (data != null) {
			writeVarInt(buf, data.length);
			buf.writeBytes(data);
		}
	}

	public String getKey() {
		return key;
	}

	public byte[] getData() {
		return data;
	}
}

