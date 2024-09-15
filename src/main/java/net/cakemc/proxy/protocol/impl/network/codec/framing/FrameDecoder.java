package net.cakemc.proxy.protocol.impl.network.codec.framing;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.DecoderException;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.List;

public class FrameDecoder extends ByteToMessageDecoder {
	private static final int MAX_PACKET_SIZE = 2097152;

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		byteBuf.markReaderIndex();

		final byte[] buf = new byte[3];
		for (int i = 0 ; i < buf.length ; i++) {
			if (!byteBuf.isReadable()) {
				byteBuf.resetReaderIndex();
				return;
			}

			buf[i] = byteBuf.readByte();
			if (buf[i] < 0)
				continue;

			ByteBuf buffer = Unpooled.wrappedBuffer(buf);
			int length = AbstractPacket.readVarInt(buffer);

			if (buffer != null)
				buffer.release();

			if (length == 0)
				throw new CorruptedFrameException("Empty packet!");

			if (byteBuf.readableBytes() < length) {
				byteBuf.resetReaderIndex();
				return;
			}

			if (length > MAX_PACKET_SIZE)
				throw new DecoderException(String.format("Packet size of %d is larger than protocol maximum of %d", length, MAX_PACKET_SIZE));

			if (byteBuf.hasMemoryAddress()) {
				list.add(byteBuf.slice(byteBuf.readerIndex(), length).retain());
				byteBuf.skipBytes(length);
			} else {
				ByteBuf dst = channelHandlerContext.alloc().directBuffer(length);
				byteBuf.readBytes(dst);
				list.add(dst);
			}
			return;
		}

		throw new CorruptedFrameException("length wider than 21-bit");
	}

}