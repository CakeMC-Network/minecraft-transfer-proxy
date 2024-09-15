package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.block.entity.BlockEntity;

public class ClientBlockEntityDataPacket extends AbstractPacket {

	private static final int POSITION_X_SIZE = 38;
	private static final int POSITION_Y_SIZE = 12;
	private static final int POSITION_Y_SHIFT = 0xFFF;
	private static final int POSITION_WRITE_SHIFT = 0x3FFFFFF;

	private BlockEntity blockState;

	public ClientBlockEntityDataPacket(BlockEntity blockState) {
		this.blockState = blockState;
	}

	public ClientBlockEntityDataPacket() {
	}

	@Override
	public void read(ByteBuf buf) {

	}

	@Override
	public void write(ByteBuf buf) {
		long x = blockState.getX() & POSITION_WRITE_SHIFT;
		long y = blockState.getY() & POSITION_Y_SHIFT;
		long z = blockState.getZ() & POSITION_WRITE_SHIFT;

		buf.writeLong(x << POSITION_X_SIZE | z << POSITION_Y_SIZE | y);
		writeVarInt(buf, blockState.getType().getId());

		writeNBT(buf, blockState.getNbtComponent());
	}

	public BlockEntity getBlockState() {
		return blockState;
	}

	public void setBlockState(BlockEntity blockState) {
		this.blockState = blockState;
	}
}

