package net.cakemc.proxy.protocol.impl.events.impl.connection;

import net.cakemc.proxy.protocol.impl.events.NetworkPlayerEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

import java.util.Objects;

public class PlayerVersionDefinedEvent extends NetworkPlayerEvent {

	private final int version;

	public PlayerVersionDefinedEvent(NetworkPlayer networkPlayer, int version) {
		super(networkPlayer);
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		PlayerVersionDefinedEvent that = (PlayerVersionDefinedEvent) o;
		return version == that.version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), version);
	}

	public int getVersion() {
		return version;
	}
}
