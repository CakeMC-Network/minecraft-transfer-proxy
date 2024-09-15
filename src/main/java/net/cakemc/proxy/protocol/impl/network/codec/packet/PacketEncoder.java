package net.cakemc.proxy.protocol.impl.network.codec.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.api.packets.AbstractPacketRegistry;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

/**
 * The type Packet encoder.
 */
public class PacketEncoder extends MessageToByteEncoder<AbstractPacket> {

	private final NetworkPlayer player;
	private final AbstractPacketRegistry packetRegistry;

	/**
	 * Instantiates a new Packet encoder.
	 *
	 * @param player         the player
	 * @param packetRegistry the packet registry
	 */
	public PacketEncoder(NetworkPlayer player, AbstractPacketRegistry packetRegistry) {
		this.player = player;
		this.packetRegistry = packetRegistry;
	}

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, AbstractPacket abstractPacket, ByteBuf byteBuf) throws Exception {
		int writeIndexStart = byteBuf.writerIndex();

		Class<? extends AbstractPacket> packetClass = abstractPacket.getClass();
		int packetId = packetRegistry.byPacketClass(player.getCurrentState(), packetClass);

		if (packetId != -1) {
			AbstractPacket.writeVarInt(byteBuf, packetId);

			abstractPacket.write(byteBuf);

			return;
		}
		byteBuf.writerIndex(writeIndexStart);
	}

}
