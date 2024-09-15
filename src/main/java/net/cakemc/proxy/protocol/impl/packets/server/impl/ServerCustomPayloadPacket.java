package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.Arrays;

public class ServerCustomPayloadPacket extends AbstractPacket {

	private String channel;
	private byte[] data;

	public ServerCustomPayloadPacket(String channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	}

	public ServerCustomPayloadPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.channel = readString(buf);
		this.data = readByteArray(buf, buf.readableBytes());
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, channel);
		writeByteArray(buf, data);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "ServerCustomPayloadPacket{" +
		       "channel='" + channel + '\'' +
		       ", data=" + Arrays.toString(data) +
		       '}';
	}
}

