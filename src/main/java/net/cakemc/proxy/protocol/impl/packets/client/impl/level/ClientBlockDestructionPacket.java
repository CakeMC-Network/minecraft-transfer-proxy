package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.BlockBreakStage;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.WorldPosition;

public class ClientBlockDestructionPacket extends AbstractPacket {

	private int breakerEntityId;
	private WorldPosition position;
	private BlockBreakStage stage;

	public ClientBlockDestructionPacket(int breakerEntityId, WorldPosition position, BlockBreakStage stage) {
		this.breakerEntityId = breakerEntityId;
		this.position = position;
		this.stage = stage;
	}


	public ClientBlockDestructionPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.breakerEntityId = readVarInt(buf);
		this.position = readPosition(buf);
		this.stage = readBlockBreakStage(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.breakerEntityId);
		writePosition(buf, this.position);
		writeBlockBreakStage(buf, this.stage);
	}

	public BlockBreakStage readBlockBreakStage(ByteBuf buf) {
		int stage = buf.readUnsignedByte();
		if (stage >= 0 && stage < 10) {
			return BlockBreakStage.STAGES[stage];
		} else {
			return BlockBreakStage.RESET;
		}
	}

	public void writeBlockBreakStage(ByteBuf buf, BlockBreakStage stage) {
		if (stage == BlockBreakStage.RESET) {
			buf.writeByte(255);
		} else {
			buf.writeByte(stage.ordinal());
		}
	}

	public int getBreakerEntityId() {
		return breakerEntityId;
	}

	public WorldPosition getPosition() {
		return position;
	}

	public BlockBreakStage getStage() {
		return stage;
	}
}

