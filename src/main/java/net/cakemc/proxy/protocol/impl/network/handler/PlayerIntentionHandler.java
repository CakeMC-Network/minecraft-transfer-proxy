package net.cakemc.proxy.protocol.impl.network.handler;

import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol;
import net.cakemc.proxy.protocol.impl.events.impl.connection.PlayerVersionDefinedEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;
import net.cakemc.proxy.protocol.impl.packets.server.impl.ClientIntentionPacket;

public class PlayerIntentionHandler {

	private final NetworkPlayer networkPlayer;
	private final EventManager eventManager;
	private Origin origin;

	public PlayerIntentionHandler(NetworkPlayer networkPlayer, EventManager eventManager) {
		this.networkPlayer = networkPlayer;
		this.eventManager = eventManager;
	}

	public void handleIntent(AbstractPacket abstractPacket) {
		if (abstractPacket instanceof ClientIntentionPacket packet) {
			// status
			switch (packet.getState()) {
				case 1 -> networkPlayer.setCurrentState(AbstractProtocol.State.STATUS);
				// login
				case 2 -> {
					networkPlayer.setCurrentState(AbstractProtocol.State.LOGIN);
					origin = Origin.LOGIN;

					this.eventManager.call(new PlayerVersionDefinedEvent(networkPlayer, packet.getProtocolVersion()));
				}
				// transfer
				case 3 -> {
					networkPlayer.setCurrentState(AbstractProtocol.State.LOGIN);
					origin = Origin.TRANSFER;

					this.eventManager.call(new PlayerVersionDefinedEvent(networkPlayer, packet.getProtocolVersion()));
				}
			}

		}
	}

	public Origin getOrigin() {
		return origin;
	}

	public enum Origin {
		LOGIN, TRANSFER
	}
}
