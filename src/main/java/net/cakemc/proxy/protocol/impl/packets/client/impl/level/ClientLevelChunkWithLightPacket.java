package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.nbt.NBTComponent;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.LightUpdateData;
import net.cakemc.mc.lib.world.block.entity.BlockEntity;

import java.util.List;

public class ClientLevelChunkWithLightPacket extends AbstractPacket {

	private int chunkX;
	private int chunkZ;
	private NBTComponent heightmaps;
	private byte[] data;
	private LightUpdateData lightData;
	private List<BlockEntity> blockEntities;
	private boolean skipCache;

	public ClientLevelChunkWithLightPacket(
		 int chunkX, int chunkZ, NBTComponent heightmaps,
		 byte[] data, LightUpdateData lightData,
		 List<BlockEntity> blockEntities
	) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.heightmaps = heightmaps;
		this.data = data;
		this.lightData = lightData;
		this.blockEntities = blockEntities;

		this.skipCache = false;
	}

	public ClientLevelChunkWithLightPacket() {
	}

	public static int getChunkCoordX(long index) {
		return (int) (index >> 32);
	}

	public static int chunkPositionXFromBlockIndex(final int index) {
		return index & 0xF;
	}

	public static int chunkPositionYFromBlockIndex(final int index) {
		var y = (index & 0x07FFFFF0) >>> 4;
		if (((index >>> 27) & 1) == 1) {
			y = -y;
		}
		return y;
	}

	public static int chunkPositionZFromBlockIndex(final int index) {
		return (index >> 28) & 0xF;
	}

	@Override
	public void read(ByteBuf buf) {
		//todo maybe usefull in future
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeInt(this.chunkX);
		buf.writeInt(this.chunkZ);
		writeNBT(buf, this.heightmaps);

		writeVarInt(buf, this.data.length);
		buf.writeBytes(this.data);

		writeVarInt(buf, this.blockEntities.size());

		for (BlockEntity blockEntity : blockEntities) {
			int positionIndex = blockEntity.getIndex();

			int x = chunkPositionXFromBlockIndex(positionIndex);
			int y = chunkPositionYFromBlockIndex(positionIndex);
			int z = chunkPositionZFromBlockIndex(positionIndex);

			buf.writeByte((byte) (((x & 15) << 4) | ((z & 0xF) & 15)));

			buf.writeShort((short) y);
			writeVarInt(buf, blockEntity.getType().getId());
			writeNBT(buf, blockEntity.getNbtComponent());
		}

		lightData.write(buf);
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

	public NBTComponent getHeightmaps() {
		return heightmaps;
	}

	public void setHeightmaps(NBTComponent heightmaps) {
		this.heightmaps = heightmaps;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public LightUpdateData getLightData() {
		return lightData;
	}

	public void setLightData(LightUpdateData lightData) {
		this.lightData = lightData;
	}

	public boolean isSkipCache() {
		return skipCache;
	}

	public void setSkipCache(boolean skipCache) {
		this.skipCache = skipCache;
	}
}

