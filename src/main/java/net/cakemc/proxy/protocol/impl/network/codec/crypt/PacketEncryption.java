package net.cakemc.proxy.protocol.impl.network.codec.crypt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * The type Packet encryption.
 */
public class PacketEncryption extends MessageToByteEncoder<ByteBuf> {

	private final Cipher cipher;
	private byte[] encryptedArray = new byte[0];

	/**
	 * Instantiates a new Packet encryption.
	 *
	 * @param key the key
	 */
	public PacketEncryption(Key key) {
		try {
			this.cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			this.cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
		         InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf object, ByteBuf byteBuf2) throws Exception {
		int length = object.readableBytes();
		byte[] bytes = new byte[length];
		object.readBytes(bytes, 0, length);

		int outLength = cipher.getOutputSize(length);

		if (this.encryptedArray.length < outLength) {
			this.encryptedArray = new byte[outLength];
		}

		byteBuf2.writeBytes(this.encryptedArray, 0, cipher.update(bytes, 0, length, this.encryptedArray, 0));
	}

}
