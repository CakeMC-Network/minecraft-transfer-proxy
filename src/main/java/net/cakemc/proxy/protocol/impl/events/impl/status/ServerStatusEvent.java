package net.cakemc.proxy.protocol.impl.events.impl.status;

import net.cakemc.mc.lib.game.Status;
import net.cakemc.mc.lib.game.event.Cancelable;
import net.cakemc.proxy.protocol.impl.events.NetworkPlayerEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

import java.util.Objects;

public class ServerStatusEvent extends NetworkPlayerEvent implements Cancelable {

	private Status.Info info;
	private boolean cancelled;

	public ServerStatusEvent(NetworkPlayer player, Status.Info info) {
		super(player);

		this.info = info;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		ServerStatusEvent that = (ServerStatusEvent) o;
		return cancelled == that.cancelled && Objects.equals(info, that.info);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), info, cancelled);
	}

	@Override
	public String toString() {
		return "ServerStatusEvent{" +
		       "info=" + info +
		       ", cancelled=" + cancelled +
		       '}';
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	public Status.Info getInfo() {
		return info;
	}

	public void setInfo(Status.Info info) {
		this.info = info;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public boolean setCancelState(boolean state) {
		return cancelled = state;
	}
}
