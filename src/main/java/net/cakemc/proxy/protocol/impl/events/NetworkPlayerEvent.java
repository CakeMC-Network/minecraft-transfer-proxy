package net.cakemc.proxy.protocol.impl.events;

import net.cakemc.mc.lib.game.event.AbstractEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

import java.util.Objects;

public abstract class NetworkPlayerEvent extends AbstractEvent {

	private final NetworkPlayer networkPlayer;

	protected NetworkPlayerEvent(NetworkPlayer networkPlayer) {
		this.networkPlayer = networkPlayer;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NetworkPlayerEvent that = (NetworkPlayerEvent) o;
		return Objects.equals(networkPlayer, that.networkPlayer);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(networkPlayer);
	}

	public NetworkPlayer getNetworkPlayer() {
		return networkPlayer;
	}
}
