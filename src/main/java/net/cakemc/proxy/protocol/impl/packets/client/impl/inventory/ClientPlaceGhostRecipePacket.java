package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientPlaceGhostRecipePacket extends AbstractPacket {
	private int containerId;
	private String recipeId;

	public ClientPlaceGhostRecipePacket(int containerId, String recipeId) {
		this.containerId = containerId;
		this.recipeId = recipeId;
	}

	public ClientPlaceGhostRecipePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.containerId = buf.readByte();
		this.recipeId = readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.containerId);
		writeString(buf, this.recipeId);
	}

	public int getContainerId() {
		return containerId;
	}

	public String getRecipeId() {
		return recipeId;
	}
}

