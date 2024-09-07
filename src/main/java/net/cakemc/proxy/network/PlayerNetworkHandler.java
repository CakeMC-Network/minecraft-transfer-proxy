package net.cakemc.proxy.network;

import net.cakemc.library.network.AbstractPacket;

/**
 * The type Player network handler.
 */
public abstract class PlayerNetworkHandler {

    /**
     * The Network player.
     */
    protected final ProxyPlayer proxyPlayer;

    /**
     * Instantiates a new Player network handler.
     *
     * @param proxyPlayer the network player
     */
    protected PlayerNetworkHandler(ProxyPlayer proxyPlayer) {
		this.proxyPlayer = proxyPlayer;
	}

    /**
     * Initialize.
     */
    public abstract void initialize();

    /**
     * Packet received.
     *
     * @param abstractPacket the abstract packet
     */
    public abstract void packetReceived(AbstractPacket abstractPacket);

    /**
     * Send packet.
     *
     * @param abstractPacket the abstract packet
     */
    public void sendPacket(AbstractPacket abstractPacket) {
		this.proxyPlayer.sendPacket(abstractPacket);
	}

    /**
     * On disconnect.
     */
    public abstract void onDisconnect();
}
