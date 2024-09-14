package net.cakemc.proxy;

import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.protocol.impl.MinecraftProtocol;
import net.cakemc.proxy.network.NetworkServer;
import net.cakemc.screensystem.logging.Logger;

import java.io.IOException;

/**
 * The type Redirect proxy server.
 */
public final class RedirectProxyServer extends AbstractProxyServer {

	/**
	 * The constant PROTOCOL.
	 */
	public static final MinecraftProtocol PROTOCOL;
	/**
	 * The constant SERVER_ICON.
	 */
	public static final byte[] SERVER_ICON;

	static {
		PROTOCOL = new MinecraftProtocol();
		PROTOCOL.register();

		try {
			SERVER_ICON = RedirectProxyServer.class.getResourceAsStream("/server-icon.png").readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private NetworkServer networkServer;
	private final String host;
	private final int port;
	private final String identifier;
	private final EventManager eventManager;

	@Override
	public Logger getLogger() {
		return Logger.SERVICE_LOGGER.supply(RedirectProxyServer.class);
	}

	/**
	 * Instantiates a new Redirect proxy server.
	 *
	 * @param host       the host
	 * @param port       the port
	 * @param identifier the identifier
	 */
	public RedirectProxyServer(final String host, final int port, String identifier) {
		this.host = host;
		this.port = port;
		this.identifier = identifier;

		eventManager = new EventManager();
	}

	@Override public void start() {
		try {
			networkServer = new NetworkServer(host, port, eventManager, PROTOCOL);
			networkServer.start();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override public void close() {
		this.networkServer.getBossGroup().close();
		this.networkServer.getWorkerGroup().close();
	}

	@Override public EventManager getEventManager() {
		return eventManager;
	}

	/**
	 * Gets network server.
	 *
	 * @return the network server
	 */
	public NetworkServer getNetworkServer() {
		return networkServer;
	}

	@Override public String getHost() {
		return host;
	}

	@Override public int getPort() {
		return port;
	}

	@Override public String getIdentifier() {
		return identifier;
	}
}
