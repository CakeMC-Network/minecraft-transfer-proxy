package net.cakemc.proxy.protocol.impl.packets.server.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerPlaceRecipePacket extends AbstractPacket {

	private int containerId;
	private String recipeId;
	private boolean makeAll;

	public ServerPlaceRecipePacket(int containerId, String recipeId, boolean makeAll) {
		this.containerId = containerId;
		this.recipeId = recipeId;
		this.makeAll = makeAll;
	}

	public ServerPlaceRecipePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.containerId = buf.readByte();
		this.recipeId = readString(buf);
		this.makeAll = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.containerId);
		writeString(buf, this.recipeId);
		buf.writeBoolean(this.makeAll);
	}

	public int getContainerId() {
		return containerId;
	}

	public String getRecipeId() {
		return recipeId;
	}

	public boolean isMakeAll() {
		return makeAll;
	}
}

