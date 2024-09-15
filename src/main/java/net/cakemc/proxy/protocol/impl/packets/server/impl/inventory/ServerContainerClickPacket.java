package net.cakemc.proxy.protocol.impl.packets.server.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.inventory.*;
import net.cakemc.mc.lib.game.inventory.item.ItemStack;
import net.cakemc.mc.lib.network.AbstractPacket;
import speiger.src.collections.ints.maps.impl.hash.Int2ObjectOpenHashMap;
import speiger.src.collections.ints.maps.interfaces.Int2ObjectMap;

import java.util.Map;

public class ServerContainerClickPacket extends AbstractPacket {

	private int containerId;
	private int stateId;
	private int slot;
	private ContainerActionType action;
	private ContainerAction param;
	private ItemStack carriedItem;
	private Map<Integer, ItemStack> changedSlots;

	public ServerContainerClickPacket(
		 int containerId, int stateId, int slot,
		 ContainerActionType action, ContainerAction param,
		 ItemStack carriedItem, Int2ObjectMap<ItemStack> changedSlots
	) {
		this.containerId = containerId;
		this.stateId = stateId;
		this.slot = slot;
		this.action = action;
		this.param = param;
		this.carriedItem = carriedItem;
		this.changedSlots = changedSlots;
	}

	public ServerContainerClickPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.containerId = buf.readByte();
		this.stateId = readVarInt(buf);
		this.slot = buf.readShort();
		byte param = buf.readByte();
		this.action = ContainerActionType.values()[buf.readByte()];

		if (this.action == ContainerActionType.CLICK_ITEM) {
			this.param = ClickItemAction.values()[param];
		} else if (this.action == ContainerActionType.SHIFT_CLICK_ITEM) {
			this.param = ShiftClickItemAction.values()[param];
		} else if (this.action == ContainerActionType.MOVE_TO_HOTBAR_SLOT) {
			this.param = MoveToHotbarAction.values()[param];
		} else if (this.action == ContainerActionType.CREATIVE_GRAB_MAX_STACK) {
			this.param = CreativeGrabAction.values()[param];
		} else if (this.action == ContainerActionType.DROP_ITEM) {
			this.param = DropItemAction.values()[param + (this.slot != -1 ? 2 : 0)];
		} else if (this.action == ContainerActionType.SPREAD_ITEM) {
			this.param = SpreadItemAction.values()[param];
		} else if (this.action == ContainerActionType.FILL_STACK) {
			this.param = FillStackAction.values()[param];
		} else {
			throw new IllegalStateException();
		}

		int changedItemsSize = readVarInt(buf);
		this.changedSlots = new Int2ObjectOpenHashMap<>(changedItemsSize);
		for (int i = 0 ; i < changedItemsSize ; i++) {
			int key = buf.readShort();
			ItemStack value = readOptionalItemStack(buf);
			this.changedSlots.put(key, value);
		}

		this.carriedItem = readOptionalItemStack(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.containerId);
		writeVarInt(buf, this.stateId);
		buf.writeShort(this.slot);

		int param = this.param.getId();
		if (this.action == ContainerActionType.DROP_ITEM) {
			param %= 2;
		}

		buf.writeByte(param);
		buf.writeByte(this.action.ordinal());

		writeVarInt(buf, this.changedSlots.size());
		for (Map.Entry<Integer, ItemStack> pair : this.changedSlots.entrySet()) {
			buf.writeShort(pair.getKey());
			writeOptionalItemStack(buf, pair.getValue());
		}

		writeOptionalItemStack(buf, this.carriedItem);
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

	public ContainerActionType getAction() {
		return action;
	}

	public void setAction(ContainerActionType action) {
		this.action = action;
	}

	public ContainerAction getParam() {
		return param;
	}

	public void setParam(ContainerAction param) {
		this.param = param;
	}

	public ItemStack getCarriedItem() {
		return carriedItem;
	}

	public void setCarriedItem(ItemStack carriedItem) {
		this.carriedItem = carriedItem;
	}

	public Map<Integer, ItemStack> getChangedSlots() {
		return changedSlots;
	}

	public void setChangedSlots(Int2ObjectMap<ItemStack> changedSlots) {
		this.changedSlots = changedSlots;
	}
}

