package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientKeepAlivePacket extends AbstractPacket {
	private long pingId;

	public ClientKeepAlivePacket() {
	}

	public ClientKeepAlivePacket(long pingId) {
		this.pingId = pingId;
	}

	@Override
	public void read(ByteBuf buf) {
		this.pingId = buf.readLong();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeLong(pingId);
	}

	public long getPingId() {
		return pingId;
	}

	public void setPingId(long pingId) {
		this.pingId = pingId;
	}
}

