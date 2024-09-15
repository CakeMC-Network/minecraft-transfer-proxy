package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientContainerSetDataPacket extends AbstractPacket {

	private int inventoryId;
	private int rawProperties;
	private int value;

	public ClientContainerSetDataPacket(int inventoryId, int rawProperties, int value) {
		this.inventoryId = inventoryId;
		this.rawProperties = rawProperties;
		this.value = value;
	}

	public ClientContainerSetDataPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.inventoryId = buf.readByte();
		this.rawProperties = buf.readShort();
		this.value = buf.readShort();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.inventoryId);
		buf.writeShort(this.rawProperties);
		buf.writeShort(this.value);
	}

	public int getInventoryId() {
		return inventoryId;
	}

	public int getRawProperties() {
		return rawProperties;
	}

	public int getValue() {
		return value;
	}
}

