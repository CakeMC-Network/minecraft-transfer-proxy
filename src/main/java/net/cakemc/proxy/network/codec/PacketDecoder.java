package net.cakemc.proxy.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.protocol.api.packets.AbstractPacketRegistry;
import net.cakemc.proxy.network.ProxyPlayer;

import java.util.List;

/**
 * The type Packet decoder.
 */
public class PacketDecoder extends ByteToMessageDecoder {

	private final ProxyPlayer player;
	private final AbstractPacketRegistry packetRegistry;

    /**
     * Instantiates a new Packet decoder.
     *
     * @param player         the player
     * @param packetRegistry the packet registry
     */
    public PacketDecoder(ProxyPlayer player, AbstractPacketRegistry packetRegistry) {
		this.player = player;
		this.packetRegistry = packetRegistry;
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		int readIndex = byteBuf.readerIndex();

		int id = AbstractPacket.readVarInt(byteBuf);

		if (id == -1) {
			byteBuf.readerIndex(readIndex);
			return;
		}

		AbstractPacket packet = packetRegistry.construct(player.getCurrentState(), id);

		if (packet == null) {
			throw new DecoderException("packet not found for id %s".formatted(id));
		}

		packet.read(byteBuf);

		if (byteBuf.readableBytes() > 0) {

			throw new DecoderException("buffer still contains data for packet: %d - %s bytes: %d"
				                            .formatted(id, packet.getClass().getSimpleName(), byteBuf.readableBytes()));


		}

		if (byteBuf.readableBytes() != 0)
			throw new DecoderException("packet wasn't fully read! left over: %d".formatted(byteBuf.readableBytes()));

		list.add(packet);
	}
}
