package net.cakemc.proxy.protocol.impl.network.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.List;
import java.util.zip.Inflater;

/**
 * The type Compression decoder.
 */
public class CompressionDecoder extends ByteToMessageDecoder {

	private final Inflater inflater = new Inflater();

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf object, List<Object> list) throws Exception {
		if (object.readableBytes() != 0) {
			int size = AbstractPacket.readVarInt(object);
			if (size == 0) {
				list.add(object.readBytes(object.readableBytes()));

			} else {
				byte[] bytes = new byte[object.readableBytes()];
				object.readBytes(bytes);
				this.inflater.setInput(bytes);
				byte[] inflated = new byte[size];
				this.inflater.inflate(inflated);
				list.add(Unpooled.wrappedBuffer(inflated));
				this.inflater.reset();
			}
		}
	}

	@Override
	protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved0(ctx);

		inflater.end();
	}

	/**
	 * Gets inflater.
	 *
	 * @return the inflater
	 */
	public Inflater getInflater() {
		return inflater;
	}
}
