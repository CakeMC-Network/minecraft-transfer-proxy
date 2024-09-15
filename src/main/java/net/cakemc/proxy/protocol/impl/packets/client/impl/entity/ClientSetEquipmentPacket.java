package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.inventory.item.EquipmentSlot;
import net.cakemc.mc.lib.game.inventory.item.EquipmentSlotGroup;
import net.cakemc.mc.lib.game.inventory.item.ItemStack;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

public class ClientSetEquipmentPacket extends AbstractPacket {

	private int entityId;
	private EquipmentSlot[] items;

	public ClientSetEquipmentPacket(int entityId, EquipmentSlot[] items) {
		this.entityId = entityId;
		this.items = items;
	}

	public ClientSetEquipmentPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		boolean hasNextEntry = true;
		List<EquipmentSlot> list = new ArrayList<>();
		while (hasNextEntry) {
			int rawSlot = buf.readByte();
			EquipmentSlotGroup slot = EquipmentSlotGroup.values()[((byte) rawSlot) & 127];
			ItemStack item = readOptionalItemStack(buf);
			list.add(new EquipmentSlot(slot, item));
			hasNextEntry = (rawSlot & 128) == 128;
		}
		this.items = list.toArray(new EquipmentSlot[0]);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, entityId);

		for (int i = 0 ; i < this.items.length ; i++) {
			int rawSlot = this.items[i].getGroup().ordinal();
			if (i != items.length - 1) {
				rawSlot = rawSlot | 128;
			}
			buf.writeByte(rawSlot);
			writeOptionalItemStack(buf, this.items[i].getStack());
		}
	}

	public int getEntityId() {
		return entityId;
	}

	public EquipmentSlot[] getItems() {
		return items;
	}
}

