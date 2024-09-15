package net.cakemc.proxy.protocol.impl.network.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.impl.MinecraftProtocol;

import java.util.zip.Deflater;

/**
 * The type Compression encoder.
 */
public class CompressionEncoder extends MessageToByteEncoder<ByteBuf> {
	/**
	 * The constant MAX_UNCOMPRESSED_SIZE.
	 */
	public static final int MAX_UNCOMPRESSED_SIZE = 8388608;

	/**
	 * The Buffer.
	 */
	protected final byte[] buffer = new byte[8192];

	/**
	 * The Deflater.
	 */
	protected final Deflater deflater = new Deflater();

	/**
	 * Gets max uncompressed size.
	 *
	 * @return the max uncompressed size
	 */
	public static int getMaxUncompressedSize() {
		return MAX_UNCOMPRESSED_SIZE;
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);

		this.deflater.end();
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf object, ByteBuf out) {
		int readable = object.readableBytes();
		if (readable > MAX_UNCOMPRESSED_SIZE) {
			throw new EncoderException("packet size is over maximum (%s/%s)"
				                            .formatted(readable, MAX_UNCOMPRESSED_SIZE));
		}

		if (readable < MinecraftProtocol.DEFAULT_SERVER_THRESHOLD) {
			AbstractPacket.writeVarInt(out, 0);
			out.writeBytes(object);
		} else {
			byte[] bytes = new byte[readable];
			object.readBytes(bytes);

			AbstractPacket.writeVarInt(out, bytes.length);

			this.deflater.setInput(bytes, 0, readable);
			this.deflater.finish();
			while (!this.deflater.finished()) {
				int length = this.deflater.deflate(this.buffer);
				out.writeBytes(this.buffer, 0, length);
			}

			this.deflater.reset();
		}
	}

	/**
	 * Get buffer byte [ ].
	 *
	 * @return the byte [ ]
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * Gets deflater.
	 *
	 * @return the deflater
	 */
	public Deflater getDeflater() {
		return deflater;
	}

}
