package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientCooldownPacket extends AbstractPacket {

	private int itemId;
	private int cooldownTicks;

	public ClientCooldownPacket(int itemId, int cooldownTicks) {
		this.itemId = itemId;
		this.cooldownTicks = cooldownTicks;
	}

	public ClientCooldownPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.itemId = readVarInt(buf);
		this.cooldownTicks = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, itemId);
		writeVarInt(buf, cooldownTicks);
	}

	public int getCooldownTicks() {
		return cooldownTicks;
	}

	public int getItemId() {
		return itemId;
	}
}

