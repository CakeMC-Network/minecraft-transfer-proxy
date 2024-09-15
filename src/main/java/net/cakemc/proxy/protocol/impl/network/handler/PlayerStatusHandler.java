package net.cakemc.proxy.protocol.impl.network.handler;

import net.cakemc.mc.lib.game.Status;
import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.mc.lib.game.text.rewrite.TextBuilder;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.impl.events.impl.status.ServerStatusEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientPongResponsePacket;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientStatusResponsePacket;
import net.cakemc.proxy.protocol.impl.packets.server.impl.ServerPingRequestPacket;
import net.cakemc.proxy.protocol.impl.packets.server.impl.ServerStatusRequestPacket;

import java.util.ArrayList;

public class PlayerStatusHandler {

	public final EventManager eventManager;
	private final NetworkPlayer networkPlayer;

	public PlayerStatusHandler(NetworkPlayer networkPlayer, EventManager eventManager) {
		this.networkPlayer = networkPlayer;
		this.eventManager = eventManager;
	}

	public void handleStatus(AbstractPacket abstractPacket) {
		if (abstractPacket instanceof ServerStatusRequestPacket) {
			Status.Info info = new Status.Info(TextBuilder.of("This is our custom minecraft server instance!").build(),
			                                   new Status.PlayerList(100, 0, new ArrayList<>()),
			                                   new Status.Version("Custom", 747), new byte[0], false
			);

			ServerStatusEvent serverStatusEvent = new ServerStatusEvent(networkPlayer, info);
			this.eventManager.call(serverStatusEvent);

			if (serverStatusEvent.isCancelled())
				return;

			networkPlayer.sendPacket(new ClientStatusResponsePacket(serverStatusEvent.getInfo()));
		} else if (abstractPacket instanceof ServerPingRequestPacket packet) {
			networkPlayer.sendPacket(new ClientPongResponsePacket(packet.getTime()));
		}
	}

}
