package net.cakemc.proxy.protocol.impl.network.codec.framing;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.cakemc.mc.lib.network.AbstractPacket;

/**
 * The type Minecraft frame encoder.
 */
public class FrameEncoder extends MessageToByteEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf packet, ByteBuf output) throws Exception {
		int length = packet.readableBytes();
		int varLength = AbstractPacket.getVarIntLength(length);

		output.ensureWritable(length + varLength);

		AbstractPacket.writeVarInt(output, length);
		output.writeBytes(packet);
	}

}
