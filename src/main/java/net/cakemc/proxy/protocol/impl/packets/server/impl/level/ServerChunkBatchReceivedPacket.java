package net.cakemc.proxy.protocol.impl.packets.server.impl.level;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerChunkBatchReceivedPacket extends AbstractPacket {

	private float desiredChunksPerTick;

	public ServerChunkBatchReceivedPacket(float desiredChunksPerTick) {
		this.desiredChunksPerTick = desiredChunksPerTick;
	}

	public ServerChunkBatchReceivedPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.desiredChunksPerTick = buf.readFloat();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeFloat(desiredChunksPerTick);
	}

	public float getDesiredChunksPerTick() {
		return desiredChunksPerTick;
	}

}

