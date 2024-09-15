package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.BitSet;

public class ServerChatPacket extends AbstractPacket {

	private String message;
	private long timeStamp;
	private long salt;
	private byte[] signature;
	private int offset;
	private BitSet acknowledgedMessages;

	public ServerChatPacket(String message, long timeStamp, long salt, byte @Nullable [] signature, int offset, BitSet acknowledgedMessages) {
		this.message = message;
		this.timeStamp = timeStamp;
		this.salt = salt;
		this.signature = signature;
		this.offset = offset;
		this.acknowledgedMessages = acknowledgedMessages;
	}

	public ServerChatPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.message = readString(buf);
		this.timeStamp = buf.readLong();
		this.salt = buf.readLong();
		if (buf.readBoolean()) {
			byte[] signature = new byte[256];
			buf.readBytes(signature);
			this.signature = signature;
		}

		this.offset = readVarInt(buf);
		this.acknowledgedMessages = readFixedBitSet(buf, 20);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.message);
		buf.writeLong(this.timeStamp);
		buf.writeLong(this.salt);

		if (this.signature != null) {
			buf.writeBytes(signature);
		}

		writeVarInt(buf, this.offset);
		writeFixedBitSet(buf, this.acknowledgedMessages, 20);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getSalt() {
		return salt;
	}

	public void setSalt(long salt) {
		this.salt = salt;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public BitSet getAcknowledgedMessages() {
		return acknowledgedMessages;
	}

	public void setAcknowledgedMessages(BitSet acknowledgedMessages) {
		this.acknowledgedMessages = acknowledgedMessages;
	}

	@Override public String toString() {
		return "ServerChatPacket{" +
		       "message='" + message + '\'' +
		       ", timeStamp=" + timeStamp +
		       ", salt=" + salt +
		       ", signature=" + Arrays.toString(signature) +
		       ", offset=" + offset +
		       ", acknowledgedMessages=" + acknowledgedMessages +
		       '}';
	}
}

