package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerPingRequestPacket extends AbstractPacket {

	private long time;

	public ServerPingRequestPacket(long time) {
		this.time = time;
	}

	public ServerPingRequestPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.time = buf.readLong();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeLong(this.time);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}

