package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientChunkBatchFinishedPacket extends AbstractPacket {

	private int batchSize;

	public ClientChunkBatchFinishedPacket(int batchSize) {
		this.batchSize = batchSize;
	}

	public ClientChunkBatchFinishedPacket() {
	}

	@Override public void read(ByteBuf buf) {
		batchSize = readVarInt(buf);
	}

	@Override public void write(ByteBuf buf) {
		writeVarInt(buf, batchSize);
	}

	public int getBatchSize() {
		return batchSize;
	}

}

