package net.cakemc.proxy.protocol.impl.network.handler;

import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.mc.lib.game.nbt.NBTComponent;
import net.cakemc.mc.lib.game.nbt.NBTType;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol;
import net.cakemc.proxy.protocol.impl.events.impl.connection.PlayerConfigEvent;
import net.cakemc.proxy.protocol.impl.events.impl.connection.PlayerConnectEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientFinishConfigurationPacket;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientRegistryDataPacket;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientRegistryDataPacket.RegistryEntry;
import net.cakemc.proxy.protocol.impl.packets.server.impl.ServerFinishConfigurationPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerConfigHandler {

	private final NetworkPlayer networkPlayer;
	private final EventManager eventManager;

	public PlayerConfigHandler(NetworkPlayer networkPlayer, EventManager eventManager) {
		this.networkPlayer = networkPlayer;
		this.eventManager = eventManager;
	}

	public void handleConfiguration(AbstractPacket abstractPacket) {
		if (abstractPacket instanceof ServerFinishConfigurationPacket) {
			this.networkPlayer.setCurrentState(AbstractProtocol.State.GAME);

			this.eventManager.call(new PlayerConnectEvent(networkPlayer));
		}
	}

	public void sendServerConfiguration() {
		this.eventManager.call(new PlayerConfigEvent(networkPlayer));

		NBTComponent nbtComponent = this.networkPlayer.getProtocol().getClientDefaultRegistry();
		for (Map.Entry<String, NBTType<?>> entry : nbtComponent.getMap().entrySet()) {
			NBTComponent entryTag = (NBTComponent) entry.getValue();
			String typeTag = entryTag.getStringTag("type").getValue();
			List<NBTType<?>> valueTag = entryTag.getListTag("value").getValue();
			List<RegistryEntry> entries = new ArrayList<>();

			for (NBTType<?> compoundTag : valueTag) {
				if (compoundTag instanceof NBTComponent component) {
					String nameTag = component.getStringTag("name").getValue();
					int id = component.getIntegerTag("id").getValue();
					entries.add(id, new RegistryEntry(nameTag, component.getComponentTag("element")));
				}


			}

			networkPlayer.sendPacket(new ClientRegistryDataPacket(typeTag, entries));
		}

		networkPlayer.sendPacket(new ClientFinishConfigurationPacket());
	}

}
