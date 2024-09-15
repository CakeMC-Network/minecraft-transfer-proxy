package net.cakemc.proxy.protocol.impl.packets.server.impl.inventory;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerPickItemPacket extends AbstractPacket {

	private int slot;

	public ServerPickItemPacket(int slot) {
		this.slot = slot;
	}

	public ServerPickItemPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		slot = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, slot);
	}

	public int getSlot() {
		return slot;
	}
}

