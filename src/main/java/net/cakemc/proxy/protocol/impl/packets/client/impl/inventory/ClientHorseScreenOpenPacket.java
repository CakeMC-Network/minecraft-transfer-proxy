package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientHorseScreenOpenPacket extends AbstractPacket {
	private int inventoryId;
	private int inventorySize;
	private int entityId;

	public ClientHorseScreenOpenPacket(int inventoryId, int inventorySize, int entityId) {
		this.inventoryId = inventoryId;
		this.inventorySize = inventorySize;
		this.entityId = entityId;
	}

	public ClientHorseScreenOpenPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.inventoryId = buf.readByte();
		this.inventorySize = readVarInt(buf);
		this.entityId = buf.readInt();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.inventoryId);
		writeVarInt(buf, this.inventorySize);
		buf.writeInt(this.entityId);
	}

	public int getInventoryId() {
		return inventoryId;
	}

	public int getEntityId() {
		return entityId;
	}

	public int getInventorySize() {
		return inventorySize;
	}


}

