package net.cakemc.proxy.protocol.impl.network.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageCodec;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.List;

public class PacketCompressionCodec extends MessageToMessageCodec<ByteBuf, ByteBuf> {
	private static final int MAX_UNCOMPRESSED_SIZE = 8 * 1024 * 1024; // 8MiB

	private final PacketCompression compression;
	private final boolean validateDecompression;
	private final int compressionThreshold;

	public PacketCompressionCodec(PacketCompression compression, boolean validateDecompression, int compressionThreshold) {
		this.compression = compression;
		this.validateDecompression = validateDecompression;

		this.compressionThreshold = compressionThreshold;
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		this.compression.close();
	}

	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
		int uncompressed = msg.readableBytes();
		if (uncompressed > MAX_UNCOMPRESSED_SIZE) {
			throw new IllegalArgumentException("Packet too big (is " + uncompressed + ", should be less than " + MAX_UNCOMPRESSED_SIZE + ")");
		}

		ByteBuf outBuf = ctx.alloc().directBuffer(uncompressed);
		if (uncompressed < compressionThreshold) {
			// Under the threshold, there is nothing to do.
			AbstractPacket.writeVarInt(outBuf, 0);
			outBuf.writeBytes(msg);
		} else {
			AbstractPacket.writeVarInt(outBuf, uncompressed);
			compression.deflate(msg, outBuf);
		}

		out.add(outBuf);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		int claimedUncompressedSize = AbstractPacket.readVarInt(in);
		if (claimedUncompressedSize == 0) {
			out.add(in.retain());
			return;
		}

		if (validateDecompression) {
			if (claimedUncompressedSize < compressionThreshold) {
				throw new DecoderException("Badly compressed packet - size of " + claimedUncompressedSize + " is below server threshold of " + compressionThreshold);
			}

			if (claimedUncompressedSize > MAX_UNCOMPRESSED_SIZE) {
				throw new DecoderException("Badly compressed packet - size of " + claimedUncompressedSize + " is larger than protocol maximum of " + MAX_UNCOMPRESSED_SIZE);
			}
		}

		ByteBuf uncompressed = ctx.alloc().directBuffer(claimedUncompressedSize);
		try {
			compression.inflate(in, uncompressed, claimedUncompressedSize);
			out.add(uncompressed);
		} catch (Exception e) {
			uncompressed.release();
			throw new DecoderException("Failed to decompress packet", e);
		}
	}
}