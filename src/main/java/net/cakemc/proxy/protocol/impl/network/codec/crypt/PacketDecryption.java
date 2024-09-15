package net.cakemc.proxy.protocol.impl.network.codec.crypt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * The type Packet decryption.
 */
public class PacketDecryption extends ByteToMessageDecoder {

	private final Cipher cipher;
	/**
	 * The Decrypted data.
	 */
	protected byte[] decryptedData = new byte[0];

	/**
	 * Instantiates a new Packet decryption.
	 *
	 * @param key the key
	 */
	public PacketDecryption(Key key) {
		try {
			this.cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			this.cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
		         InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		int length = byteBuf.readableBytes();
		byte[] bytes = this.getBytes(byteBuf);

		ByteBuf result = channelHandlerContext.alloc()
		                                      .heapBuffer(this.cipher.getOutputSize(length));

		result.writerIndex(this.cipher.update(bytes, 0, length, result.array(), result.arrayOffset()));

		list.add(result);
	}

	@Override
	protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved0(ctx);
	}

	private byte[] getBytes(ByteBuf buf) {
		int length = buf.readableBytes();
		if (this.decryptedData.length < length) {
			this.decryptedData = new byte[length];
		}

		buf.readBytes(this.decryptedData, 0, length);
		return this.decryptedData;
	}

}
