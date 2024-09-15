package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.text.MessageSignature;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientDeleteChatPacket extends AbstractPacket {

	private MessageSignature signature;

	public ClientDeleteChatPacket(MessageSignature signature) {
		this.signature = signature;
	}

	public ClientDeleteChatPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.signature = MessageSignature.read(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		AbstractPacket.writeVarInt(buf, this.signature.getId() + 1);
		if (signature.getMessageSignature() != null) {
			buf.writeBytes(signature.getMessageSignature());
		}
	}

	public MessageSignature getSignature() {
		return signature;
	}

	public void setSignature(MessageSignature signature) {
		this.signature = signature;
	}
}

