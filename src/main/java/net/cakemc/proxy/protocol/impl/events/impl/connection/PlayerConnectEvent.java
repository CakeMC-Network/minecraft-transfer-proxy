package net.cakemc.proxy.protocol.impl.events.impl.connection;

import net.cakemc.proxy.protocol.impl.events.NetworkPlayerEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

public class PlayerConnectEvent extends NetworkPlayerEvent {

	public PlayerConnectEvent(NetworkPlayer networkPlayer) {
		super(networkPlayer);
	}

}
