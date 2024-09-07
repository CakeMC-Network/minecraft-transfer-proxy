package net.cakemc.proxy.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.cakemc.library.network.AbstractPacket;
import net.cakemc.protocol.api.packets.AbstractPacketRegistry;
import net.cakemc.protocol.impl.packets.server.impl.ClientIntentionPacket;
import net.cakemc.proxy.network.ProxyPlayer;

/**
 * The type Packet encoder.
 */
public class PacketEncoder extends MessageToByteEncoder<AbstractPacket> {

	private final ProxyPlayer player;
	private final AbstractPacketRegistry packetRegistry;

    /**
     * Instantiates a new Packet encoder.
     *
     * @param player         the player
     * @param packetRegistry the packet registry
     */
    public PacketEncoder(ProxyPlayer player, AbstractPacketRegistry packetRegistry) {
		this.player = player;
		this.packetRegistry = packetRegistry;
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, AbstractPacket abstractPacket, ByteBuf byteBuf) throws Exception {
		int writeIndexStart = byteBuf.writerIndex();

		Class<? extends AbstractPacket> packetClass = abstractPacket.getClass();
		int packetId = packetRegistry.byPacketClass(player.getCurrentState(), packetClass);

		if (abstractPacket instanceof ClientIntentionPacket) {
			packetId = 0;
		}

		if (packetId != -1) {
			AbstractPacket.writeVarInt(byteBuf, packetId);

			abstractPacket.write(byteBuf);

			return;
		}

		byteBuf.writerIndex(writeIndexStart);
	}

}
