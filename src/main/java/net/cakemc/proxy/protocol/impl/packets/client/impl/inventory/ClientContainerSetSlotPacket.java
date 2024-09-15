package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.inventory.item.ItemStack;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientContainerSetSlotPacket extends AbstractPacket {

	private int containerId;
	private int stateId;
	private int slot;
	private ItemStack item;

	public ClientContainerSetSlotPacket(int containerId, int stateId, int slot, ItemStack item) {
		this.containerId = containerId;
		this.stateId = stateId;
		this.slot = slot;
		this.item = item;
	}

	public ClientContainerSetSlotPacket() {
	}

	@Override public void read(ByteBuf buf) {
		this.containerId = buf.readUnsignedByte();
		this.stateId = readVarInt(buf);
		this.slot = buf.readShort();
		this.item = readOptionalItemStack(buf);
	}

	@Override public void write(ByteBuf buf) {
		buf.writeByte(this.containerId);
		writeVarInt(buf, this.stateId);
		buf.writeShort(this.slot);
		writeOptionalItemStack(buf, this.item);
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

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}
}

