package net.cakemc.proxy.protocol.impl.packets.client.impl.player;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientBlockChangedAckPacket extends AbstractPacket {

	private int sequence;

	public ClientBlockChangedAckPacket(int sequence) {
		this.sequence = sequence;
	}

	public ClientBlockChangedAckPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		sequence = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, sequence);
	}

	public int getSequence() {
		return sequence;
	}
}

