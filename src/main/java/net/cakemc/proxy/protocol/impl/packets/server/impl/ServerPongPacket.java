package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerPongPacket extends AbstractPacket {
	private int pong;

	public ServerPongPacket() {
	}

	public ServerPongPacket(int pong) {
		this.pong = pong;
	}

	@Override
	public void read(ByteBuf buf) {
		this.pong = buf.readInt();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeInt(pong);
	}

	public int getPong() {
		return pong;
	}
}

