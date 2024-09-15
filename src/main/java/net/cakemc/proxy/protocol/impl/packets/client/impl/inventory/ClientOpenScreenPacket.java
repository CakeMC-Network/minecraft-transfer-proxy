package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.inventory.ContainerType;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientOpenScreenPacket extends AbstractPacket {
	private int containerId;
	private ContainerType type;
	private BaseComponent title;

	public ClientOpenScreenPacket(int containerId, ContainerType type, BaseComponent title) {
		this.containerId = containerId;
		this.type = type;
		this.title = title;
	}

	public ClientOpenScreenPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.containerId = readVarInt(buf);
		this.type = ContainerType.values()[readVarInt(buf)];
		this.title = readComponent(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.containerId);
		writeVarInt(buf, this.type.ordinal());
		writeComponent(buf, this.title);
	}

	public BaseComponent getTitle() {
		return title;
	}

	public ContainerType getType() {
		return type;
	}

	public int getContainerId() {
		return containerId;
	}

}

