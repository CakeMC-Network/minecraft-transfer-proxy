package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.ServerLink;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientServerLinksPacket extends AbstractPacket {

	private ServerLink[] links;

	public ClientServerLinksPacket(ServerLink... links) {
		this.links = links;
	}

	public ClientServerLinksPacket() {
	}

	@Override
	public void read(ByteBuf buf) {

	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, links.length);
		for (ServerLink link : links) {
			buf.writeBoolean(link.getType() != null);
			if (link.getType() != null) {
				writeVarInt(buf, link.getType().ordinal());
			} else {
				writeComponent(buf, link.getUnknownType());
			}

			writeString(buf, link.getLink());
		}
	}

}

