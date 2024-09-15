package net.cakemc.proxy.protocol.impl.packets.client.impl.title;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetSubtitleTextPacket extends AbstractPacket {


	private BaseComponent baseComponent;

	public ClientSetSubtitleTextPacket() {
	}

	public ClientSetSubtitleTextPacket(BaseComponent baseComponent) {
		this.baseComponent = baseComponent;
	}

	@Override public void read(ByteBuf buf) {
		this.baseComponent = readComponent(buf);
	}

	@Override public void write(ByteBuf buf) {
		writeComponent(buf, baseComponent);
	}

	public BaseComponent getBaseComponent() {
		return baseComponent;
	}
}
