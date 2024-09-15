package net.cakemc.proxy.protocol.impl.network.codec.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.api.packets.AbstractPacketRegistry;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

import java.util.List;

/**
 * The type Packet decoder.
 */
public class PacketDecoder extends ByteToMessageDecoder {

	private final NetworkPlayer player;
	private final AbstractPacketRegistry packetRegistry;

	/**
	 * Instantiates a new Packet decoder.
	 *
	 * @param player         the player
	 * @param packetRegistry the packet registry
	 */
	public PacketDecoder(NetworkPlayer player, AbstractPacketRegistry packetRegistry) {
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

		if (packet == null)
			throw new DecoderException("packet can't be constructed for id: %d".formatted(id));

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
