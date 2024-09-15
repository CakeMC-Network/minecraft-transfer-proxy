package net.cakemc.proxy.protocol.impl.network.codec.crypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Crypt units.
 */
public class CryptUnits {

	/**
	 * Generate key pair key pair.
	 *
	 * @return the key pair
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(1024);
			return gen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Failed to generate server key pair.", e);
		}
	}

	/**
	 * Generate pass byte [ ].
	 *
	 * @return the byte [ ]
	 */
	public static byte[] generatePass() {
		byte[] pass = new byte[4];
		ThreadLocalRandom.current().nextBytes(pass);
		return pass;
	}

	/**
	 * Run encryption byte [ ].
	 *
	 * @param mode the mode
	 * @param key  the key
	 * @param pass the pass
	 *
	 * @return the byte [ ]
	 */
	public static byte[] runEncryption(int mode, Key key, byte[] pass) {
		try {
			Cipher cipher = Cipher.getInstance(key.getAlgorithm().equals("RSA") ? "RSA/ECB/PKCS1Padding" : "AES/CFB8/NoPadding");
			cipher.init(mode, key);
			return cipher.doFinal(pass);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Failed to " + (mode == Cipher.DECRYPT_MODE ? "decrypt" : "encrypt") + " data.", e);
		}
	}

	/**
	 * Gets secret key.
	 *
	 * @param privateKey the private key
	 * @param sharedKey  the shared key
	 *
	 * @return the secret key
	 */
	public static SecretKey getSecretKey(PrivateKey privateKey, byte[] sharedKey) {
		return new SecretKeySpec(runEncryption(Cipher.DECRYPT_MODE, privateKey, sharedKey), "AES");
	}

	/**
	 * Get encrypted pass byte [ ].
	 *
	 * @param privateKey    the private key
	 * @param encryptedPass the encrypted pass
	 *
	 * @return the byte [ ]
	 */
	public static byte[] getEncryptedPass(PrivateKey privateKey, byte[] encryptedPass) {
		return runEncryption(Cipher.DECRYPT_MODE, privateKey, encryptedPass);
	}

	/**
	 * Gets server id.
	 *
	 * @param base      the base
	 * @param publicKey the public key
	 * @param secretKey the secret key
	 *
	 * @return the server id
	 */
	public static String getServerId(String base, PublicKey publicKey, SecretKey secretKey) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(base.getBytes(StandardCharsets.ISO_8859_1));
			digest.update(secretKey.getEncoded());
			digest.update(publicKey.getEncoded());
			return new BigInteger(digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Server ID hash algorithm unavailable.", e);
		}
	}

}
