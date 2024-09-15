package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerKeepAlivePacket extends AbstractPacket {
	private long id;

	public ServerKeepAlivePacket(long id) {
		this.id = id;
	}

	public ServerKeepAlivePacket() {
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeLong(id);
	}

	@Override
	public void read(ByteBuf buf) {
		this.id = buf.readLong();
	}

	public long getId() {
		return id;
	}

	@Override public String toString() {
		return "ServerKeepAlivePacket{" +
		       "id=" + id +
		       '}';
	}
}

