package net.cakemc.proxy.protocol.impl.packets.client.impl.player;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetCarriedItemPacket extends AbstractPacket {

	private int slot;

	public ClientSetCarriedItemPacket(int slot) {
		this.slot = slot;
	}

	public ClientSetCarriedItemPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.slot = buf.readByte();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.slot);
	}

	public int getSlot() {
		return slot;
	}
}

