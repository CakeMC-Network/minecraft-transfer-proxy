package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerChatCommandPacket extends AbstractPacket {

	private String command;

	public ServerChatCommandPacket() {
	}

	public ServerChatCommandPacket(String command) {
		this.command = command;
	}

	@Override
	public void read(ByteBuf buf) {
		this.command = readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.command);
	}

	public String getCommand() {
		return command;
	}
}

