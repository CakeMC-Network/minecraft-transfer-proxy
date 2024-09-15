package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.game.inventory.item.ItemStack;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientContainerSetContentPacket extends AbstractPacket {

	private int containerId;
	private int stateId;
	private ItemStack[] items;
	private ItemStack carriedItem;

	public ClientContainerSetContentPacket(int containerId, int stateId, ItemStack[] items, ItemStack carriedItem) {
		this.containerId = containerId;
		this.stateId = stateId;
		this.items = items;
		this.carriedItem = carriedItem;
	}

	public ClientContainerSetContentPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.containerId = buf.readUnsignedByte();
		this.stateId = readVarInt(buf);
		this.items = new ItemStack[readVarInt(buf)];
		for (int index = 0 ; index < this.items.length ; index++) {
			this.items[index] = readOptionalItemStack(buf);
		}
		this.carriedItem = readOptionalItemStack(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(containerId);
		writeVarInt(buf, stateId);
		writeVarInt(buf, items.length);
		for (ItemStack item : items) {
			writeOptionalItemStack(buf, item);
		}
		writeOptionalItemStack(buf, carriedItem);
	}

	public int getContainerId() {
		return containerId;
	}

	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public ItemStack[] getItems() {
		return items;
	}

	public void setItems(ItemStack[] items) {
		this.items = items;
	}

	public ItemStack getCarriedItem() {
		return carriedItem;
	}

	public void setCarriedItem(ItemStack carriedItem) {
		this.carriedItem = carriedItem;
	}
}

