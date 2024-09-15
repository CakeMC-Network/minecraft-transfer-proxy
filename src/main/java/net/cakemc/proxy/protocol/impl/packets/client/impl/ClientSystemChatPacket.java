package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.text.test.Serializer;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSystemChatPacket extends AbstractPacket {

	private BaseComponent component;
	private boolean overlay;

	public ClientSystemChatPacket(BaseComponent component, boolean overlay) {
		this.component = component;
		this.overlay = overlay;
	}


	public ClientSystemChatPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
	}

	@Override
	public void write(ByteBuf buf) {
		writeNBT(buf, Serializer.SERIALIZER.serialize(this.component));
		buf.writeBoolean(overlay);
	}

	public BaseComponent getComponent() {
		return component;
	}

	public boolean isOverlay() {
		return overlay;
	}

	public void setOverlay(boolean overlay) {
		this.overlay = overlay;
	}
}

