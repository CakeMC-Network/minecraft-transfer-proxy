package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerPlayerAbilitiesPacket extends AbstractPacket {
	private static final int FLAG_FLYING = 0x02;

	private boolean flying;

	public ServerPlayerAbilitiesPacket(boolean flying) {
		this.flying = flying;
	}

	public ServerPlayerAbilitiesPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		byte flags = buf.readByte();
		this.flying = (flags & FLAG_FLYING) > 0;
	}

	@Override
	public void write(ByteBuf buf) {
		int flags = 0;

		if (this.flying) {
			flags |= FLAG_FLYING;
		}

		buf.writeByte(flags);
	}

	public boolean isFlying() {
		return flying;
	}
}

