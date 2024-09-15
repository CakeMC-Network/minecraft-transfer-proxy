package net.cakemc.proxy.protocol.impl.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.proxy.protocol.impl.MinecraftProtocol;
import net.cakemc.proxy.protocol.impl.network.codec.framing.FrameDecoder;
import net.cakemc.proxy.protocol.impl.network.codec.framing.FrameEncoder;
import net.cakemc.proxy.protocol.impl.network.codec.packet.PacketDecoder;
import net.cakemc.proxy.protocol.impl.network.codec.packet.PacketEncoder;
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
	private final boolean onlineMode;

	public NetworkInitializer(MinecraftProtocol protocol, EventManager eventManager, boolean onlineMode) {
		this.protocol = protocol;
		this.eventManager = eventManager;
		this.onlineMode = onlineMode;
	}

	@Override
	protected void initChannel(@NotNull Channel channel) {
		ChannelPipeline channelPipeline = channel.pipeline();

		NetworkPlayer networkPlayer = new NetworkPlayer(channel, protocol, eventManager, onlineMode);

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
		channelPipeline.addLast(CodecOrder.MINECRAFT_DECODER.getHandlerName(), new PacketDecoder(networkPlayer, protocol.getOutbounds().getPacketRegistry()));
		channelPipeline.addLast(CodecOrder.MINECRAFT_ENCODER.getHandlerName(), new PacketEncoder(networkPlayer, protocol.getInbounds().getPacketRegistry()));

		// network-player
		channelPipeline.addLast(CodecOrder.NETWORK_PLAYER.getHandlerName(), networkPlayer);
	}
}
