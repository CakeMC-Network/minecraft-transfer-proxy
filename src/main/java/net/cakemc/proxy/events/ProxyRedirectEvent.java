package net.cakemc.proxy.events;

import net.cakemc.mc.lib.game.event.AbstractEvent;
import net.cakemc.mc.lib.game.event.Cancelable;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.proxy.network.ProxyPlayer;

import java.util.Objects;

/**
 * The type Proxy redirect event.
 */
public class ProxyRedirectEvent extends AbstractEvent implements Cancelable {

	private final ProxyPlayer player;
	private String host;
	private int port;
	private BaseComponent cancelReason;

	private boolean cancel;

    /**
     * Instantiates a new Proxy redirect event.
     *
     * @param player       the player
     * @param host         the host
     * @param port         the port
     * @param cancelReason the cancel reason
     */
    public ProxyRedirectEvent(ProxyPlayer player, String host, int port, BaseComponent cancelReason) {
		this.player = player;
		this.host = host;
		this.port = port;
		this.cancelReason = cancelReason;
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
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
		return host;
	}

    /**
     * Gets player.
     *
     * @return the player
     */
    public ProxyPlayer getPlayer() {
		return player;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProxyRedirectEvent that = (ProxyRedirectEvent) o;
		return port == that.port && cancel == that.cancel && Objects.equals(player, that.player) && Objects.equals(host, that.host) && Objects.equals(cancelReason, that.cancelReason);
	}

	@Override
	public int hashCode() {
		return Objects.hash(player, host, port, cancelReason, cancel);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public boolean setCancelState(boolean state) {
		return cancel = state;
	}

    /**
     * Gets cancel reason.
     *
     * @return the cancel reason
     */
    public BaseComponent getCancelReason() {
		return cancelReason;
	}

    /**
     * Sets cancel reason.
     *
     * @param cancelReason the cancel reason
     */
    public void setCancelReason(BaseComponent cancelReason) {
		this.cancelReason = cancelReason;
	}

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
		this.host = host;
	}

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port) {
		this.port = port;
	}
}
