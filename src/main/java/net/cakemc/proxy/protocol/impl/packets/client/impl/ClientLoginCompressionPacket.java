package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientLoginCompressionPacket extends AbstractPacket {

	private int threshold;

	public ClientLoginCompressionPacket(int threshold) {
		this.threshold = threshold;
	}

	public ClientLoginCompressionPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		threshold = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, threshold);
	}

	@Override
	public String toString() {
		return "ClientLoginCompressionPacket{" +
		       "threshold=" + threshold +
		       '}';
	}
}
