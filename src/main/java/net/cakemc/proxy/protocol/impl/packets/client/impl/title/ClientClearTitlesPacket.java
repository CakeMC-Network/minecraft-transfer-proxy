package net.cakemc.proxy.protocol.impl.packets.client.impl.title;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientClearTitlesPacket extends AbstractPacket {

	private boolean reset;


	public ClientClearTitlesPacket(boolean reset) {
		this.reset = reset;
	}

	public ClientClearTitlesPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		buf.writeBoolean(reset);
	}

	@Override
	public void write(ByteBuf buf) {
		this.reset = buf.readBoolean();
	}

	public boolean isReset() {
		return reset;
	}
}

