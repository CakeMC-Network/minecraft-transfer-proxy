package net.cakemc.proxy.protocol.impl.events.impl.connection;

import net.cakemc.proxy.protocol.impl.events.NetworkPlayerEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

public class PlayerConfigEvent extends NetworkPlayerEvent {

	public PlayerConfigEvent(NetworkPlayer networkPlayer) {
		super(networkPlayer);
	}

}
