package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.WorldPosition;

public class ClientBlockUpdatePacket extends AbstractPacket {

	private int x, y, z;
	private int block;

	public ClientBlockUpdatePacket(int x, int y, int z, int block) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = block;
	}

	public ClientBlockUpdatePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		WorldPosition worldPosition = this.readPosition(buf);
		this.x = worldPosition.getX();
		this.y = worldPosition.getY();
		this.z = worldPosition.getZ();

		this.block = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writePosition(buf, new WorldPosition(x, y, z));
		writeVarInt(buf, block);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}
}

