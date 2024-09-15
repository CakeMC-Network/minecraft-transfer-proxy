package net.cakemc.proxy.protocol.impl.network.handler;

import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.PlayerProfile.Property;
import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.mc.lib.game.text.test.api.chat.TextComponent;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.api.auth.AuthService;
import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol;
import net.cakemc.proxy.protocol.impl.MinecraftProtocol;
import net.cakemc.proxy.protocol.impl.events.impl.connection.PlayerLoginCompleteEvent;
import net.cakemc.proxy.protocol.impl.events.impl.connection.PlayerLoginStartEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;
import net.cakemc.proxy.protocol.impl.network.codec.crypt.CryptUnits;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientGameProfilePacket;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientHelloPacket;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientLoginCompressionPacket;
import net.cakemc.proxy.protocol.impl.packets.server.impl.ServerHelloPacket;
import net.cakemc.proxy.protocol.impl.packets.server.impl.ServerKeyPacket;
import net.cakemc.proxy.protocol.impl.packets.server.impl.ServerLoginAcknowledgedPacket;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Arrays;

public class PlayerLoginHandler {

	private final NetworkPlayer networkPlayer;
	private final EventManager eventManager;
	private final boolean onlineMode;

	private final byte[] pass;
	private final KeyPair keyPair;

	public PlayerLoginHandler(NetworkPlayer networkPlayer, EventManager eventManager, boolean onlineMode) {
		this.networkPlayer = networkPlayer;
		this.eventManager = eventManager;
		this.onlineMode = onlineMode;

		{
			this.pass = CryptUnits.generatePass();
			this.keyPair = CryptUnits.generateKeyPair();
		}
	}

	public void handleLogin(AbstractPacket abstractPacket) {
		if (abstractPacket instanceof ServerHelloPacket packet) {
			PlayerLoginStartEvent startEvent = new PlayerLoginStartEvent(networkPlayer);
			this.eventManager.call(startEvent);
			if (startEvent.isCancelled()) {
				this.networkPlayer.disconnect(new TextComponent("login cancelled!"));
				return;
			}

			this.networkPlayer.setAccountName(packet.getUsername());
			this.networkPlayer.setPlayerUUID(packet.getUuid());


			if (onlineMode) {
				AuthService.get().byUUID(packet.getUuid().toString()).thenAcceptAsync(user -> {
					PlayerProfile profile = new PlayerProfile(packet.getUuid().toString(), user.name());
					Arrays.stream(user.properties())
					      .map(properties -> new Property(properties.name(), properties.value(), properties.signature()))
					      .forEach(property -> profile.getProperties().add(property));
					networkPlayer.setProfile(profile);

					ClientHelloPacket helloPacket = new ClientHelloPacket(
						 "", keyPair.getPublic(), this.pass, true
					);
					networkPlayer.sendPacket(helloPacket);
				}).join();

			} else {
				// offline profile
				AuthService.get().byUUID(packet.getUuid().toString()).thenAcceptAsync(user -> {
					if (user != null) {
						PlayerProfile profile = new PlayerProfile(packet.getUuid().toString(), user.name());
						Arrays.stream(user.properties())
						      .map(properties -> new Property(properties.name(), properties.value(), properties.signature()))
						      .forEach(property -> profile.getProperties().add(property));
						networkPlayer.setProfile(profile);
						networkPlayer.sendPacket(new ClientGameProfilePacket(
							 networkPlayer.getPlayerUUID(), networkPlayer.getProfile()
						));

						return;
					}
					networkPlayer.setProfile(new PlayerProfile(packet.getUuid().toString(), packet.getUsername()));

					networkPlayer.sendPacket(new ClientGameProfilePacket(
						 networkPlayer.getPlayerUUID(), networkPlayer.getProfile()
					));
				}).join();


			}

			eventManager.call(new PlayerLoginCompleteEvent(networkPlayer));
		}
		if (abstractPacket instanceof ServerKeyPacket packet) {
			SecretKey secretKey = CryptUnits.getSecretKey(this.keyPair.getPrivate(), packet.getSharedKey());

			boolean matching = Arrays.equals(this.pass, CryptUnits.getEncryptedPass(keyPair.getPrivate(), packet.getEncryptedPass()));
			if (!matching)
				throw new IllegalStateException("keys miss matching!");

			this.networkPlayer.setEncryptionHandler(secretKey);

			this.networkPlayer.sendPacket(new ClientLoginCompressionPacket(-1));
			//this.networkPlayer.sendPacket(new ClientLoginCompressionPacket(MinecraftProtocol.DEFAULT_SERVER_THRESHOLD));
			//this.networkPlayer.setCompressionHandler();

			ClientGameProfilePacket gameProfilePacket = new ClientGameProfilePacket(networkPlayer.getPlayerUUID(), networkPlayer.getProfile());
			this.networkPlayer.sendPacket(gameProfilePacket);
		}

		if (abstractPacket instanceof ServerLoginAcknowledgedPacket) {
			networkPlayer.setCurrentState(AbstractProtocol.State.CONFIG);

			networkPlayer.sendConfiguration();
		}
	}

	public byte[] getPass() {
		return pass;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}
}
