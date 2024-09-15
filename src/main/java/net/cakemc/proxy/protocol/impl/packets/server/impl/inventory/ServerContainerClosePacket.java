package net.cakemc.proxy.protocol.impl.packets.server.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerContainerClosePacket extends AbstractPacket {

	private int inventoryId;

	public ServerContainerClosePacket(int inventoryId) {
		this.inventoryId = inventoryId;
	}

	public ServerContainerClosePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		inventoryId = buf.readByte();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(inventoryId);
	}

	public int getInventoryId() {
		return inventoryId;
	}
}

