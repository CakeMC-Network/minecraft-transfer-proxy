package net.cakemc.proxy.protocol.impl.network;

import net.cakemc.mc.lib.network.AbstractPacket;

/**
 * The type Player network handler.
 */
public abstract class PlayerNetworkHandler {

	/**
	 * The Network player.
	 */
	protected final NetworkPlayer networkPlayer;

	/**
	 * Instantiates a new Player network handler.
	 *
	 * @param networkPlayer the network player
	 */
	protected PlayerNetworkHandler(NetworkPlayer networkPlayer) {
		this.networkPlayer = networkPlayer;
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
		this.networkPlayer.sendPacket(abstractPacket);
	}

	/**
	 * On disconnect.
	 */
	public abstract void onDisconnect();
}
