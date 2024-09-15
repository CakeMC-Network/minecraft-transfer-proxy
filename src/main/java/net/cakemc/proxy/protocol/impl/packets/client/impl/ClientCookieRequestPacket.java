package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientCookieRequestPacket extends AbstractPacket {

	private String key;

	public ClientCookieRequestPacket(String key) {
		this.key = key;
	}

	public ClientCookieRequestPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.key = readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, key);
	}

	public String getKey() {
		return key;
	}
}

