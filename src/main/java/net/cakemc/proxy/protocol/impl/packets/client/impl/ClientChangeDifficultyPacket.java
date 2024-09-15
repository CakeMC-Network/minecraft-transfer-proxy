package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Difficulty;

public class ClientChangeDifficultyPacket extends AbstractPacket {

	private Difficulty difficulty;
	private boolean difficultyLock;

	public ClientChangeDifficultyPacket(Difficulty difficulty, boolean difficultyLock) {
		this.difficulty = difficulty;
		this.difficultyLock = difficultyLock;
	}

	public ClientChangeDifficultyPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.difficulty = Difficulty.values()[buf.readUnsignedByte()];
		this.difficultyLock = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.difficulty.ordinal());
		buf.writeBoolean(this.difficultyLock);
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public boolean isDifficultyLock() {
		return difficultyLock;
	}
}

