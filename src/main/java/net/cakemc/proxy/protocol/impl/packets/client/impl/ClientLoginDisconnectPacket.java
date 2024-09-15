package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientLoginDisconnectPacket extends AbstractPacket {

	private BaseComponent message;

	public ClientLoginDisconnectPacket(BaseComponent message) {
		this.message = message;
	}

	public ClientLoginDisconnectPacket() {
	}

	@Override
	public void write(ByteBuf buf) {
		writeComponent(buf, message);
	}

	@Override
	public void read(ByteBuf buf) {
		this.message = readComponent(buf);
	}

	public BaseComponent getMessage() {
		return message;
	}
}
