package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientPongResponsePacket extends AbstractPacket {

	private long time;

	public ClientPongResponsePacket(long time) {
		this.time = time;
	}

	public ClientPongResponsePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.time = buf.readLong();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeLong(time);
	}

	@Override
	public String toString() {
		return "ClientPongResponsePacket{" +
		       "time=" + time +
		       '}';
	}
}

