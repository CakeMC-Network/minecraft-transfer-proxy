package net.cakemc.proxy.protocol.impl.packets.server.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerContainerSlotStateChangedPacket extends AbstractPacket {

	private int slotId;
	private int containerId;
	private boolean newState;

	public ServerContainerSlotStateChangedPacket(int slotId, int containerId, boolean newState) {
		this.slotId = slotId;
		this.containerId = containerId;
		this.newState = newState;
	}

	public ServerContainerSlotStateChangedPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.slotId = readVarInt(buf);
		this.containerId = readVarInt(buf);
		this.newState = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.slotId);
		writeVarInt(buf, this.containerId);
		buf.writeBoolean(this.newState);
	}

	public int getSlotId() {
		return slotId;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public int getContainerId() {
		return containerId;
	}

	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}

	public boolean isNewState() {
		return newState;
	}

	public void setNewState(boolean newState) {
		this.newState = newState;
	}
}

