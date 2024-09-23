package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.ClientCommand;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerClientCommandPacket extends AbstractPacket {

	private ClientCommand clientCommand;

	@Override
	public void read(ByteBuf buf) {
		clientCommand = ClientCommand.values()[readVarInt(buf)];
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, clientCommand.ordinal());
	}

	public ClientCommand getClientCommand() {
		return clientCommand;
	}
}

