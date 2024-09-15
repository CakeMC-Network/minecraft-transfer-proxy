package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.LightUpdateData;

public class ClientLightUpdatePacket extends AbstractPacket {

	private int chunkX;
	private int chunkZ;
	private LightUpdateData lightUpdateData;

	public ClientLightUpdatePacket(int chunkX, int chunkZ, LightUpdateData lightUpdateData) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.lightUpdateData = lightUpdateData;
	}

	@Override
	public void read(ByteBuf buf) {
		this.chunkX = readVarInt(buf);
		this.chunkZ = readVarInt(buf);
		this.lightUpdateData = new LightUpdateData(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, chunkX);
		writeVarInt(buf, chunkZ);
		lightUpdateData.write(buf);
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

	public LightUpdateData getLightUpdateData() {
		return lightUpdateData;
	}

	public void setLightUpdateData(LightUpdateData lightUpdateData) {
		this.lightUpdateData = lightUpdateData;
	}
}

