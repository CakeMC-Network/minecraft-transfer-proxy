package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientStoreCookiePacket extends AbstractPacket {

	private String key;
	private byte[] data;

	public ClientStoreCookiePacket(String key, byte[] data) {
		this.key = key;
		this.data = data;
	}

	public ClientStoreCookiePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.key = readString(buf);
		int length = readVarInt(buf);
		data = new byte[length];
		buf.readBytes(data);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, key);
		writeVarInt(buf, data.length);
		buf.writeBytes(data);
	}

	public String getKey() {
		return key;
	}

	public byte[] getData() {
		return data;
	}
}

