package net.cakemc.proxy.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.cakemc.library.game.event.EventManager;
import net.cakemc.protocol.impl.MinecraftProtocol;
import net.cakemc.protocol.impl.network.CodecOrder;
import net.cakemc.protocol.impl.network.codec.framing.FrameDecoder;
import net.cakemc.protocol.impl.network.codec.framing.FrameEncoder;
import net.cakemc.proxy.network.codec.PacketDecoder;
import net.cakemc.proxy.network.codec.PacketEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * The type Network initializer.
 */
public class NetworkInitializer extends ChannelInitializer<Channel> {
	private static final int READ_TIMEOUT_SECONDS = -1;
	private static final int WRITE_TIMEOUT_SECONDS = -1;

	private final MinecraftProtocol protocol;
	private final EventManager eventManager;

    /**
     * Instantiates a new Network initializer.
     *
     * @param protocol     the protocol
     * @param eventManager the event manager
     */
    public NetworkInitializer(MinecraftProtocol protocol, EventManager eventManager) {
		this.protocol = protocol;
		this.eventManager = eventManager;
	}

	@Override
	protected void initChannel(@NotNull Channel channel) {
		ChannelPipeline channelPipeline = channel.pipeline();

		ProxyPlayer proxyPlayer = new ProxyPlayer(channel, eventManager, protocol);

		// timeouts
		if (READ_TIMEOUT_SECONDS != -1) {
			channelPipeline.addLast(
				 CodecOrder.READ_TIMEOUT_HANDLER.getHandlerName(),
				 new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
			);
		}
		if (WRITE_TIMEOUT_SECONDS != -1) {
			channelPipeline.addLast(
				 CodecOrder.WRITE_TIMEOUT_HANDLER.getHandlerName(),
				 new ReadTimeoutHandler(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
			);
		}

		// var-int framing
		channelPipeline.addLast(CodecOrder.FRAMING_DECODER.getHandlerName(), new FrameDecoder());
		channelPipeline.addLast(CodecOrder.FRAMING_ENCODER.getHandlerName(), new FrameEncoder());

		// packet handler
		channelPipeline.addLast(CodecOrder.MINECRAFT_DECODER.getHandlerName(), new PacketDecoder(proxyPlayer, protocol.getOutbounds().getPacketRegistry()));
		channelPipeline.addLast(CodecOrder.MINECRAFT_ENCODER.getHandlerName(), new PacketEncoder(proxyPlayer, protocol.getInbounds().getPacketRegistry()));

		// network-player
		channelPipeline.addLast(CodecOrder.NETWORK_PLAYER.getHandlerName(), proxyPlayer);
	}
}
