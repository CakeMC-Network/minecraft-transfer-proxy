package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerSetCarriedItemPacket extends AbstractPacket {

	private int slot;

	public ServerSetCarriedItemPacket(int slot) {
		this.slot = slot;
	}

	public ServerSetCarriedItemPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.slot = buf.readShort();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeShort(slot);
	}

	public int getSlot() {
		return slot;
	}
}

