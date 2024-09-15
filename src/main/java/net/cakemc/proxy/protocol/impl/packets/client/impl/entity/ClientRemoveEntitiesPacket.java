package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientRemoveEntitiesPacket extends AbstractPacket {

	private int[] entities;

	public ClientRemoveEntitiesPacket(int... entities) {
		this.entities = entities;
	}

	public ClientRemoveEntitiesPacket() {

	}

	@Override
	public void read(ByteBuf buf) {
		this.entities = readVarIntArray(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarIntArray(buf, this.entities);
	}

	public int[] getEntities() {
		return entities;
	}
}

