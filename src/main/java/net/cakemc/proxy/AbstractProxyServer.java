package net.cakemc.proxy;

import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.screensystem.ScreenAbleObject;

/**
 * The type Abstract proxy server.
 */
public abstract class AbstractProxyServer implements ScreenAbleObject {
    /**
     * Start.
     */
    public abstract void start();

    /**
     * Close.
     */
    public abstract void close();

    /**
     * Gets event manager.
     *
     * @return the event manager
     */
    public abstract EventManager getEventManager();

    /**
     * Gets host.
     *
     * @return the host
     */
    public abstract String getHost();

    /**
     * Gets port.
     *
     * @return the port
     */
    public abstract int getPort();

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public abstract String getIdentifier();
}
