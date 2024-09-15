package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.impl.network.codec.crypt.CryptUnits;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Arrays;

public class ServerKeyPacket extends AbstractPacket {
	private byte[] sharedKey;
	private byte[] encryptedPass;

	public ServerKeyPacket(PublicKey publicKey, SecretKey secretKey, byte[] pass) {
		this.sharedKey = CryptUnits.runEncryption(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
		this.encryptedPass = CryptUnits.runEncryption(Cipher.ENCRYPT_MODE, publicKey, pass);
	}

	public ServerKeyPacket(byte[] sharedKey, byte[] encryptedChallenge) {
		this.sharedKey = sharedKey;
		this.encryptedPass = encryptedChallenge;
	}

	public ServerKeyPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.sharedKey = readByteArray(buf);
		this.encryptedPass = readByteArray(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeByteArray(buf, this.sharedKey);
		writeByteArray(buf, this.encryptedPass);
	}

	public byte[] getSharedKey() {
		return sharedKey;
	}

	public void setSharedKey(byte[] sharedKey) {
		this.sharedKey = sharedKey;
	}

	public byte[] getEncryptedPass() {
		return encryptedPass;
	}

	public void setEncryptedPass(byte[] encryptedPass) {
		this.encryptedPass = encryptedPass;
	}

	@Override
	public String toString() {
		return "ServerKeyPacket{" +
		       "sharedKey=" + Arrays.toString(sharedKey) +
		       ", encryptedPass=" + Arrays.toString(encryptedPass) +
		       '}';
	}
}

