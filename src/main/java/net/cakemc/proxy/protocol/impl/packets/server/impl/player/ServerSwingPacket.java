package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.Hand.Type;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerSwingPacket extends AbstractPacket {

	private Type handType;

	public ServerSwingPacket(Type handType) {
		this.handType = handType;
	}

	public ServerSwingPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.handType = Type.values()[readVarInt(buf)];
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, handType.ordinal());
	}

	public Type getHandType() {
		return handType;
	}
}

