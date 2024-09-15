package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetChunkCacheCenterPacket extends AbstractPacket {

	private int chunkX;
	private int chunkZ;

	public ClientSetChunkCacheCenterPacket(int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	public ClientSetChunkCacheCenterPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		chunkX = readVarInt(buf);
		chunkZ = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, chunkX);
		writeVarInt(buf, chunkZ);
	}

	public int getChunkX() {
		return chunkX;
	}

	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}

	public void setChunkZ(int chunkZ) {
		this.chunkZ = chunkZ;
	}
}

