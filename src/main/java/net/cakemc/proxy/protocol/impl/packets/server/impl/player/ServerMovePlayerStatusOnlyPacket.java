package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerMovePlayerStatusOnlyPacket extends AbstractPacket {

	private boolean onGround;

	public ServerMovePlayerStatusOnlyPacket(boolean onGround) {
		this.onGround = onGround;
	}

	public ServerMovePlayerStatusOnlyPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		onGround = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeBoolean(onGround);
	}

	public boolean isOnGround() {
		return onGround;
	}
}

