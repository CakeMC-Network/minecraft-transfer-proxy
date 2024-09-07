package net.cakemc.proxy.events;

import net.cakemc.library.game.Status;
import net.cakemc.library.game.Status.Info;
import net.cakemc.library.game.event.AbstractEvent;
import net.cakemc.library.game.event.Cancelable;

import java.util.Objects;

/**
 * The type Proxy status event.
 */
public class ProxyStatusEvent extends AbstractEvent implements Cancelable {

	private Status.Info info;
	private boolean cancelled;

    /**
     * Instantiates a new Proxy status event.
     *
     * @param info the info
     */
    public ProxyStatusEvent(Info info) {
		this.info = info;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProxyStatusEvent that = (ProxyStatusEvent) o;
		return Objects.equals(info, that.info);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(info);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public boolean setCancelState(boolean state) {
		return cancelled = state;
	}

    /**
     * Gets info.
     *
     * @return the info
     */
    public Info getInfo() {
		return info;
	}

    /**
     * Sets info.
     *
     * @param info the info
     */
    public void setInfo(Info info) {
		this.info = info;
	}
}
