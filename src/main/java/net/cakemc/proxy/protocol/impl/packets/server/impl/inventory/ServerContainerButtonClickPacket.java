package net.cakemc.proxy.protocol.impl.packets.server.impl.inventory;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerContainerButtonClickPacket extends AbstractPacket {

	private int containerId;
	private int buttonId;

	public ServerContainerButtonClickPacket(int containerId, int buttonId) {
		this.containerId = containerId;
		this.buttonId = buttonId;
	}

	public ServerContainerButtonClickPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.containerId = readVarInt(buf);
		this.buttonId = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.containerId);
		writeVarInt(buf, this.buttonId);
	}

	public int getContainerId() {
		return containerId;
	}

	public int getButtonId() {
		return buttonId;
	}
}

