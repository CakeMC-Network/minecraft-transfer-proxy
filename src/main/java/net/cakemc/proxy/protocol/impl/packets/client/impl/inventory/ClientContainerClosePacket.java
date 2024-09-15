package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientContainerClosePacket extends AbstractPacket {

	private int inventoryId;

	public ClientContainerClosePacket(int inventoryId) {
		this.inventoryId = inventoryId;
	}

	public ClientContainerClosePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		inventoryId = buf.readUnsignedByte();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.inventoryId);
	}

	public int getInventoryId() {
		return inventoryId;
	}
}

