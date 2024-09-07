package net.cakemc.proxy.network.handler;

import net.cakemc.library.game.PlayerProfile;
import net.cakemc.library.game.Status;
import net.cakemc.library.game.text.rewrite.TextBuilder;
import net.cakemc.library.game.text.test.api.ChatColor;
import net.cakemc.library.game.text.test.api.chat.TextComponent;
import net.cakemc.library.network.AbstractPacket;
import net.cakemc.protocol.api.auth.AuthService;
import net.cakemc.protocol.api.protocol.AbstractProtocol;
import net.cakemc.protocol.impl.network.codec.crypt.CryptUnits;
import net.cakemc.protocol.impl.packets.client.impl.*;
import net.cakemc.protocol.impl.packets.server.impl.*;
import net.cakemc.proxy.RedirectProxyServer;
import net.cakemc.proxy.events.ProxyRedirectEvent;
import net.cakemc.proxy.events.ProxyStatusEvent;
import net.cakemc.proxy.network.PlayerNetworkHandler;
import net.cakemc.proxy.network.ProxyPlayer;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Player login handler.
 */
public class PlayerLoginHandler extends PlayerNetworkHandler {

    private final boolean onlineMode;
    private byte[] pass;
    private KeyPair keyPair;

	/**
	 * Instantiates a new Player network handler.
	 *
	 * @param proxyPlayer the network player
	 * @param onlineMode  the online mode
	 */
	public PlayerLoginHandler(ProxyPlayer proxyPlayer, boolean onlineMode) {
        super(proxyPlayer);

        this.onlineMode = onlineMode;
    }

    @Override
    public void initialize() {
        this.pass = net.cakemc.protocol.impl.network.codec.crypt.CryptUnits.generatePass();
        this.keyPair = net.cakemc.protocol.impl.network.codec.crypt.CryptUnits.generateKeyPair();
    }

    @Override
    public void packetReceived(AbstractPacket abstractPacket) {
        if (abstractPacket instanceof ClientIntentionPacket packet) {
            switch (packet.getState()) {
                case 1 -> proxyPlayer.setCurrentState(AbstractProtocol.State.STATUS);
                case 2 -> proxyPlayer.setCurrentState(AbstractProtocol.State.LOGIN);
            }

        }

        switch (proxyPlayer.getCurrentState()) {
            case LOGIN -> this.handleLogin(abstractPacket);
            case STATUS -> this.handleStatus(abstractPacket);
            case CONFIG -> {
                return;
            }

            default -> throw new IllegalArgumentException("Unexpected value: " + proxyPlayer.getCurrentState());
        }
    }

	/**
	 * Handle status.
	 *
	 * @param abstractPacket the abstract packet
	 */
	public void handleStatus(AbstractPacket abstractPacket) {
        if (abstractPacket instanceof ServerStatusRequestPacket) {
            Status.Info info = new Status.Info(TextBuilder.empty().append("Default Proxy instance description!").color(ChatColor.GREEN).build(),
                    new Status.PlayerList(100, 0, new ArrayList<>()),
                    new Status.Version("cluster-instance", 747),
                    RedirectProxyServer.SERVER_ICON, false
            );
            ProxyStatusEvent statusEvent = new ProxyStatusEvent(info);

            this.proxyPlayer.getEventManager().call(statusEvent);

            if (statusEvent.isCancelled())
                return;

            sendPacket(new ClientStatusResponsePacket(info));
        } else if (abstractPacket instanceof ServerPingRequestPacket packet) {
            sendPacket(new ClientPongResponsePacket(packet.getTime()));
        }
    }

	/**
	 * Handle login.
	 *
	 * @param abstractPacket the abstract packet
	 */
	public void handleLogin(AbstractPacket abstractPacket) {
        if (abstractPacket instanceof ServerHelloPacket packet) {
            this.proxyPlayer.setAccountName(packet.getUsername());
            this.proxyPlayer.setPlayerUUID(packet.getUuid());

            if (onlineMode) {
                AuthService.get().byUUID(packet.getUuid().toString()).thenAcceptAsync(user -> {
                    PlayerProfile profile = new PlayerProfile(packet.getUuid().toString(), user.name());
                    Arrays.stream(user.properties())
                            .map(properties -> new PlayerProfile
                                    .Property(properties.name(), properties.value(), properties.signature()))
                            .forEach(property -> profile.getProperties().add(property));
                    proxyPlayer.setProfile(profile);

                    ClientHelloPacket helloPacket = new ClientHelloPacket(
                            "", keyPair.getPublic(), this.pass, true
                    );

                    proxyPlayer.sendPacket(helloPacket);
                }).join();

            } else {
                // offline profile
                AuthService.get().byUUID(packet.getUuid().toString()).thenAcceptAsync(user -> {
                    PlayerProfile profile = new PlayerProfile(packet.getUuid().toString(), user.name());
                    Arrays.stream(user.properties())
                            .map(properties -> new PlayerProfile
                                    .Property(properties.name(), properties.value(), properties.signature()))
                            .forEach(property -> profile.getProperties().add(property));
                    proxyPlayer.setProfile(profile);
                }).join();

                ClientGameProfilePacket gameProfilePacket = new ClientGameProfilePacket(
                        proxyPlayer.getPlayerUUID(), proxyPlayer.getProfile()
                );
                proxyPlayer.sendPacket(gameProfilePacket);
            }
        }
        if (abstractPacket instanceof ServerKeyPacket packet) {
            SecretKey secretKey = CryptUnits.getSecretKey(this.keyPair.getPrivate(), packet.getSharedKey());

            boolean matching = Arrays.equals(this.pass, CryptUnits.getEncryptedPass(keyPair.getPrivate(), packet.getEncryptedPass()));
            if (!matching)
                throw new IllegalStateException("keys miss matching!");

            this.proxyPlayer.setEncryptionHandler(secretKey);

            this.proxyPlayer.sendPacket(new ClientLoginCompressionPacket(-1));

            ClientGameProfilePacket gameProfilePacket = new ClientGameProfilePacket(proxyPlayer.getPlayerUUID(), proxyPlayer.getProfile());
            this.proxyPlayer.sendPacket(gameProfilePacket);
        }

        if (abstractPacket instanceof ServerLoginAcknowledgedPacket) {
            proxyPlayer.setCurrentState(AbstractProtocol.State.CONFIG);

            ProxyRedirectEvent redirectEvent = new ProxyRedirectEvent(this.proxyPlayer, "127.0.0.1", 20000,
                    new TextComponent("§cUnable to connect to server reason: connection denied"));

            this.proxyPlayer.getEventManager().call(redirectEvent);

            if (redirectEvent.isCancelled()) {
                this.proxyPlayer.disconnect(redirectEvent.getCancelReason());
                return;
            }

            this.proxyPlayer.sendPacket(new ClientTransferPacket(redirectEvent.getHost(), redirectEvent.getPort()));
        }
    }

    @Override
    public void onDisconnect() {
        //keepAliveThread.interrupt();
    }

	/**
	 * Get pass byte [ ].
	 *
	 * @return the byte [ ]
	 */
	public byte[] getPass() {
        return pass;
    }

	/**
	 * Sets pass.
	 *
	 * @param pass the pass
	 */
	public void setPass(byte[] pass) {
        this.pass = pass;
    }

	/**
	 * Gets key pair.
	 *
	 * @return the key pair
	 */
	public KeyPair getKeyPair() {
        return keyPair;
    }

	/**
	 * Sets key pair.
	 *
	 * @param keyPair the key pair
	 */
	public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

}