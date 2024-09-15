package net.cakemc.proxy.protocol.impl.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.proxy.protocol.impl.MinecraftProtocol;

/**
 * The type Network server.
 */
public class NetworkServer {
	/**
	 * The constant EPOLL.
	 */
	public static final boolean EPOLL = Epoll.isAvailable(), /**
	 * The Kqueue.
	 */
	KQUEUE = KQueue.isAvailable();

	private final String host;
	private final int port;

	private final EventLoopGroup bossGroup, workerGroup;
	private final Class<? extends ServerChannel> channel;
	private final MinecraftProtocol protocol;
	private final NetworkInitializer networkInitializer;
	private final EventManager eventManager;
	private final boolean onlineMode;
	private ServerBootstrap serverBootstrap;
	private ChannelFuture channelFuture;

	/**
	 * Instantiates a new Network server.
	 *
	 * @param protocol the packet registries
	 *
	 * @throws InterruptedException when the bootstrap or future couldn't be constructed.
	 * @apiNote starts a new Proxy Server by instantation.
	 */
	public NetworkServer(MinecraftProtocol protocol, EventManager eventManager, boolean onlineMode) throws InterruptedException {
		this("127.0.0.1", 25565, protocol, eventManager, onlineMode);
	}

	/**
	 * Instantiates a new Network server.
	 *
	 * @param port     the port you want to start it at.
	 * @param protocol the packet registries
	 *
	 * @throws InterruptedException when the bootstrap or future couldn't be constructed.
	 * @apiNote starts a new Proxy Server by instantation.
	 */
	public NetworkServer(int port, MinecraftProtocol protocol, EventManager eventManager, boolean onlineMode) throws InterruptedException {
		this("127.0.0.1", port, protocol, eventManager, onlineMode);
	}

	/**
	 * Instantiates a new Network server.
	 *
	 * @param host     the host you want to start it at.
	 * @param port     the port you want to start it at.
	 * @param protocol the packet registries
	 *
	 * @throws InterruptedException when the bootstrap or future couldn't be constructed.
	 * @apiNote starts a new Proxy Server by instantation.
	 */
	public NetworkServer(String host, int port, MinecraftProtocol protocol, EventManager eventManager, boolean onlineMode) throws InterruptedException {
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.eventManager = eventManager;
		this.onlineMode = onlineMode;

		this.networkInitializer = new NetworkInitializer(protocol, eventManager, onlineMode);

		this.bossGroup = EPOLL ? (KQUEUE ? new KQueueEventLoopGroup() : new EpollEventLoopGroup()) : new NioEventLoopGroup();
		this.workerGroup = EPOLL ? (KQUEUE ? new KQueueEventLoopGroup() : new EpollEventLoopGroup()) : new NioEventLoopGroup();
		this.channel = EPOLL ? (KQUEUE ? KQueueServerSocketChannel.class : EpollServerSocketChannel.class) : NioServerSocketChannel.class;


	}

	public void start() throws InterruptedException {
		(this.channelFuture = (this.serverBootstrap = new ServerBootstrap()
			 .channel(channel)

			 .childOption(ChannelOption.IP_TOS, 0x18)
			 .childOption(ChannelOption.TCP_NODELAY, true)
			 .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)

			 .option(ChannelOption.SO_REUSEADDR, true)

			 .group(bossGroup, workerGroup)
			 .childHandler(networkInitializer)

			 .localAddress(host, port))
			 .bind().syncUninterruptibly()).channel().closeFuture().sync();
	}

	public void stop() {
		this.channelFuture.channel().close().syncUninterruptibly();
		this.bossGroup.shutdownGracefully().syncUninterruptibly();
		this.workerGroup.shutdownGracefully().syncUninterruptibly();
	}

	/**
	 * Gets host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Gets boss group.
	 *
	 * @return the boss group
	 */
	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}

	/**
	 * Gets worker group.
	 *
	 * @return the worker group
	 */
	public EventLoopGroup getWorkerGroup() {
		return workerGroup;
	}

	/**
	 * Gets channel.
	 *
	 * @return the channel
	 */
	public Class<? extends ServerChannel> getChannel() {
		return channel;
	}

	public MinecraftProtocol getProtocol() {
		return protocol;
	}

	/**
	 * Gets server bootstrap.
	 *
	 * @return the server bootstrap
	 */
	public ServerBootstrap getServerBootstrap() {
		return serverBootstrap;
	}

	/**
	 * Gets channel future.
	 *
	 * @return the channel future
	 */
	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	/**
	 * Gets network initializer.
	 *
	 * @return the network initializer
	 */
	public NetworkInitializer getNetworkInitializer() {
		return networkInitializer;
	}
}
