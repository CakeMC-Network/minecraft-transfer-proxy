package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

public class ServerChatSessionUpdatePacket extends AbstractPacket {
	private UUID sessionId;
	private long expiresAt;
	private PublicKey publicKey;
	private byte[] keySignature;

	public ServerChatSessionUpdatePacket(UUID sessionId, long expiresAt, PublicKey publicKey, byte[] keySignature) {
		this.sessionId = sessionId;
		this.expiresAt = expiresAt;
		this.publicKey = publicKey;
		this.keySignature = keySignature;
	}

	public ServerChatSessionUpdatePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.sessionId = readUUID(buf);
		this.expiresAt = buf.readLong();
		byte[] keyBytes = readByteArray(buf);
		this.keySignature = readByteArray(buf);

		PublicKey publicKey;
		try {
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Could not decode public key.", e);
		}

		this.publicKey = publicKey;
	}

	@Override
	public void write(ByteBuf buf) {
		writeUUID(buf, this.sessionId);
		buf.writeLong(this.expiresAt);
		writeByteArray(buf, this.publicKey.getEncoded());
		writeByteArray(buf, this.keySignature);
	}


	public UUID getSessionId() {
		return sessionId;
	}

	public void setSessionId(UUID sessionId) {
		this.sessionId = sessionId;
	}

	public long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public byte[] getKeySignature() {
		return keySignature;
	}

	public void setKeySignature(byte[] keySignature) {
		this.keySignature = keySignature;
	}
}

