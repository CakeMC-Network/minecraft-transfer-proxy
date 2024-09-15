package net.cakemc.proxy.protocol.impl.events.impl.connection;

import net.cakemc.mc.lib.game.event.Cancelable;
import net.cakemc.proxy.protocol.impl.events.NetworkPlayerEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

public class PlayerLoginStartEvent extends NetworkPlayerEvent implements Cancelable {

	private boolean cancel;

	public PlayerLoginStartEvent(NetworkPlayer networkPlayer) {
		super(networkPlayer);
	}

	@Override public boolean isCancelled() {
		return cancel;
	}

	@Override public boolean setCancelState(boolean state) {
		return cancel = state;
	}
}
