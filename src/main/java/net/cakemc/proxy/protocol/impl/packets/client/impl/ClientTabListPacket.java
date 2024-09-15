package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.text.test.Serializer;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientTabListPacket extends AbstractPacket {

	private BaseComponent header;
	private BaseComponent footer;

	public ClientTabListPacket(BaseComponent header, BaseComponent footer) {
		this.header = header;
		this.footer = footer;
	}


	public ClientTabListPacket() {
	}

	@Override
	public void read(ByteBuf buf) {

	}

	@Override
	public void write(ByteBuf buf) {
		writeNBT(buf, Serializer.SERIALIZER.serialize(this.header));
		writeNBT(buf, Serializer.SERIALIZER.serialize(this.footer));
	}

	public BaseComponent getHeader() {
		return header;
	}

	public void setHeader(BaseComponent header) {
		this.header = header;
	}

	public BaseComponent getFooter() {
		return footer;
	}

	public void setFooter(BaseComponent footer) {
		this.footer = footer;
	}
}

