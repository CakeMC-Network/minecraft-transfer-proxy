package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class ClientHelloPacket extends AbstractPacket {

	private String serverId;
	private PublicKey publicKey;
	private byte[] challenge;
	private boolean shouldAuthenticate;

	public ClientHelloPacket(String serverId, PublicKey publicKey, byte[] challenge, boolean shouldAuthenticate) {
		this.serverId = serverId;
		this.publicKey = publicKey;
		this.challenge = challenge;
		this.shouldAuthenticate = shouldAuthenticate;
	}

	public ClientHelloPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.serverId = readString(buf);
		byte[] publicKey = readByteArray(buf);
		this.challenge = readByteArray(buf);
		this.shouldAuthenticate = buf.readBoolean();

		try {
			this.publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Could not decode public key.", e);
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.serverId);
		byte[] encoded = this.publicKey.getEncoded();

		writeByteArray(buf, encoded);
		writeByteArray(buf, this.challenge);

		buf.writeBoolean(this.shouldAuthenticate);
	}

	@Override
	public String toString() {
		return "ClientHelloPacket{" +
		       "serverId='" + serverId + '\'' +
		       ", publicKey=" + publicKey +
		       ", challenge=" + Arrays.toString(challenge) +
		       ", shouldAuthenticate=" + shouldAuthenticate +
		       '}';
	}

	public String getServerId() {
		return serverId;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public byte[] getChallenge() {
		return challenge;
	}

	public boolean isShouldAuthenticate() {
		return shouldAuthenticate;
	}

}

