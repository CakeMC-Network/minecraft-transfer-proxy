package net.cakemc.proxy.protocol.impl.packets.server.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.List;

public class ServerEditBookPacket extends AbstractPacket {

	private int slot;
	private List<String> pages;
	private String title;

	public ServerEditBookPacket(int slot, List<String> pages, String title) {
		this.slot = slot;
		this.pages = pages;
		this.title = title;
	}

	public ServerEditBookPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.slot = readVarInt(buf);
		this.pages = readList(buf, AbstractPacket::readString);
		this.title = readNullable(buf, AbstractPacket::readString);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, slot);
		writeList(buf, pages, AbstractPacket::writeString);
		writeNullable(buf, title, AbstractPacket::writeString);
	}

	public int getSlot() {
		return slot;
	}

	public List<String> getPages() {
		return pages;
	}

	public String getTitle() {
		return title;
	}
}

