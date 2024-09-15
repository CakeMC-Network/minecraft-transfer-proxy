package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.UUID;

public class ClientResourcePackPushPacket extends AbstractPacket {

	private UUID uuid;
	private String url;
	private String hash;
	private boolean forced;
	private BaseComponent prompt;

	public ClientResourcePackPushPacket() {
	}

	public ClientResourcePackPushPacket(UUID uuid, String url, String hash, boolean forced, BaseComponent prompt) {
		this.uuid = uuid;
		this.url = url;
		this.hash = hash;
		this.forced = forced;
		this.prompt = prompt;
	}

	@Override
	public void read(ByteBuf buf) {
		this.uuid = readUUID(buf);
		this.url = readString(buf);
		this.hash = readString(buf);
		this.forced = buf.readBoolean();

		if (buf.readBoolean())
			this.prompt = readComponent(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeUUID(buf, uuid);
		writeString(buf, url);
		writeString(buf, hash);
		buf.writeBoolean(forced);
		buf.writeBoolean(prompt != null);

		if (prompt != null)
			writeComponent(buf, prompt);
	}

	public UUID getUuid() {
		return uuid;
	}

	public BaseComponent getPrompt() {
		return prompt;
	}

	public String getHash() {
		return hash;
	}

	public String getUrl() {
		return url;
	}

}

