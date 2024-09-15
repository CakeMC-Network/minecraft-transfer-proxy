package net.cakemc.proxy.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.event.EventManager;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.api.player.PlayerConnection;
import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol;
import net.cakemc.proxy.protocol.impl.MinecraftProtocol;
import net.cakemc.proxy.protocol.impl.network.CodecOrder;
import net.cakemc.proxy.protocol.impl.network.codec.crypt.PacketDecryption;
import net.cakemc.proxy.protocol.impl.network.codec.crypt.PacketEncryption;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientDisconnectPacket;
import net.cakemc.proxy.network.handler.PlayerLoginHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.util.UUID;

/**
 * The type Proxy player.
 */
public class ProxyPlayer extends SimpleChannelInboundHandler<AbstractPacket> implements PlayerConnection {

	private final PlayerLoginHandler protocolHandler;
	private final Channel channel;
	private final EventManager eventManager;
	private final MinecraftProtocol protocol;

	private String accountName;
	private UUID playerUUID;

	private PlayerProfile profile;

	private boolean connected;

	private AbstractProtocol.State currentState;

    /**
     * Instantiates a new Network player.
     *
     * @param channel      the channel
     * @param eventManager the event manager
     * @param protocol     the protocol
     */
    public ProxyPlayer(Channel channel, EventManager eventManager, MinecraftProtocol protocol) {
		this.channel = channel;
		this.eventManager = eventManager;
		this.protocol = protocol;

		this.connected = true;
		this.currentState = AbstractProtocol.State.HANDSHAKE;

		this.protocolHandler = new PlayerLoginHandler(this, true);
		this.protocolHandler.initialize();
	}

	public void sendPacket(AbstractPacket abstractPacket) {
		if (channel == null || !channel.isActive())
			return;

		this.channel.writeAndFlush(abstractPacket).addListener((ChannelFutureListener) channelFuture -> {
			if (channelFuture.isSuccess()) {
				return;
			}

			if (channelFuture.cause() == null)
				return;
			try {
				throw channelFuture.cause();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		});
	}

	@Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof IOException)
			return;

		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void packetReceived(AbstractPacket abstractPacket) {
		protocolHandler.packetReceived(abstractPacket);
	}

    /**
     * Sets encryption handler.
     *
     * @param key the key
     */
    public void setEncryptionHandler(Key key) {
		if (checkChannelUsability()) {
			// DECRYPTION -> ENCRYPTION -> FRAMING ...
			channel.pipeline().addBefore(
				 CodecOrder.FRAMING_DECODER.getHandlerName(),
				 CodecOrder.ENCRYPTION_ENCODER.getHandlerName(),
				 new PacketEncryption(key)
			);
			channel.pipeline().addBefore(
				 CodecOrder.ENCRYPTION_ENCODER.getHandlerName(),
				 CodecOrder.DECRYPTION_DECODER.getHandlerName(),
				 new PacketDecryption(key)
			);
		}
	}

    /**
     * Sets compression handler.
     */
    public void setCompressionHandler() {
		//if (!checkChannelUsability())
		//	return;
		//// COMP_DECODER -> COMP_ENCODER -> PACKET ...
		//channel.pipeline().addBefore(
		//	 CodecOrder.MINECRAFT_DECODER.getHandlerName(),
		//	 CodecOrder.COMPRESSION_ENCODER.getHandlerName(),
		//	 new CompressionEncoder()
		//);
		//channel.pipeline().addBefore(
		//	 CodecOrder.ENCRYPTION_ENCODER.getHandlerName(),
		//	 CodecOrder.COMPRESSION_DECODER.getHandlerName(),
		//	 new CompressionDecoder()
		//);
	}

	@Override
	public void disconnect(BaseComponent baseComponent) {
		if (!checkChannelUsability())
			return;

		this.sendPacket(new ClientDisconnectPacket(baseComponent));

		this.channel.flush().close().addListener(future -> {
			this.protocolHandler.onDisconnect();
			this.connected = false;
		});
	}

	@Override
	public void handleLogin(AbstractPacket abstractPacket) {

	}

	@Override
	public void handleStatus(AbstractPacket abstractPacket) {

	}

	@Override
	public void handleConfiguration(AbstractPacket abstractPacket) {

	}

	@Override
	public void handleIntent(AbstractPacket abstractPacket) {

	}

	@Override
	public void sendConfiguration() {

	}


	/**
     * Check channel usability boolean.
     *
     * @return the boolean
     */
    public boolean checkChannelUsability() {
		return this.channel != null && this.channel.isActive();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket abstractPacket) throws Exception {
		if (!checkChannelUsability())
			return;

		this.packetReceived(abstractPacket);
	}

	@Override
	public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);

		this.connected = false;
	}

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public Channel getChannel() {
		return channel;
	}

    /**
     * Gets current state.
     *
     * @return the current state
     */
    public AbstractProtocol.State getCurrentState() {
		return currentState;
	}

    /**
     * Sets current state.
     *
     * @param currentState the current state
     */
    public void setCurrentState(AbstractProtocol.State currentState) {
		this.currentState = currentState;
	}

    /**
     * Gets protocol handler.
     *
     * @return the protocol handler
     */
    public PlayerLoginHandler getProtocolHandler() {
		return protocolHandler;
	}

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected() {
		return connected;
	}

    /**
     * Sets connected.
     *
     * @param connected the connected
     */
    public void setConnected(boolean connected) {
		this.connected = connected;
	}

    /**
     * Gets account name.
     *
     * @return the account name
     */
    public String getAccountName() {
		return accountName;
	}

    /**
     * Sets account name.
     *
     * @param accountName the account name
     */
    public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

    /**
     * Gets player uuid.
     *
     * @return the player uuid
     */
    public UUID getPlayerUUID() {
		return playerUUID;
	}

    /**
     * Sets player uuid.
     *
     * @param playerUUID the player uuid
     */
    public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

    /**
     * Gets profile.
     *
     * @return the profile
     */
    public PlayerProfile getProfile() {
		return profile;
	}

    /**
     * Sets profile.
     *
     * @param profile the profile
     */
    public void setProfile(PlayerProfile profile) {
		this.profile = profile;
	}

    /**
     * Gets protocol.
     *
     * @return the protocol
     */
    public MinecraftProtocol getProtocol() {
		return protocol;
	}


    /**
     * Get pass byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getPass() {
		return this.protocolHandler.getPass();
	}

    /**
     * Gets key pair.
     *
     * @return the key pair
     */
    public KeyPair getKeyPair() {
		return this.protocolHandler.getKeyPair();
	}

    /**
     * Gets event manager.
     *
     * @return the event manager
     */
    public EventManager getEventManager() {
		return eventManager;
	}
}
