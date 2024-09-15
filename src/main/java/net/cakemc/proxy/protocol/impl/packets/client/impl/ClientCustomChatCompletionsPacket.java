package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.text.ChatCompletionAction;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientCustomChatCompletionsPacket extends AbstractPacket {
	private ChatCompletionAction action;
	private String[] entries;

	public ClientCustomChatCompletionsPacket(ChatCompletionAction action, String[] entries) {
		this.action = action;
		this.entries = entries;
	}

	public ClientCustomChatCompletionsPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.action = ChatCompletionAction.values()[readVarInt(buf)];
		this.entries = new String[readVarInt(buf)];
		for (int i = 0 ; i < this.entries.length ; i++) {
			this.entries[i] = readString(buf);
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.action.ordinal());
		writeVarInt(buf, this.entries.length);
		for (String entry : this.entries) {
			writeString(buf, entry);
		}
	}

	public ChatCompletionAction getAction() {
		return action;
	}

	public String[] getEntries() {
		return entries;
	}
}

