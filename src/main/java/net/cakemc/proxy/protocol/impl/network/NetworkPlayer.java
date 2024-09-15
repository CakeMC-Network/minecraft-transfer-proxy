package net.cakemc.proxy.protocol.impl.network;

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
import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol.State;
import net.cakemc.proxy.protocol.impl.MinecraftProtocol;
import net.cakemc.proxy.protocol.impl.events.impl.connection.PlayerDisconnectEvent;
import net.cakemc.proxy.protocol.impl.events.impl.packet.PlayerPacketReceivedEvent;
import net.cakemc.proxy.protocol.impl.events.impl.packet.ServerSendPacketEvent;
import net.cakemc.proxy.protocol.impl.network.codec.compression.PacketCompressionCodec;
import net.cakemc.proxy.protocol.impl.network.codec.compression.ZlibCompression;
import net.cakemc.proxy.protocol.impl.network.codec.crypt.PacketDecryption;
import net.cakemc.proxy.protocol.impl.network.codec.crypt.PacketEncryption;
import net.cakemc.proxy.protocol.impl.network.handler.PlayerConfigHandler;
import net.cakemc.proxy.protocol.impl.network.handler.PlayerIntentionHandler;
import net.cakemc.proxy.protocol.impl.network.handler.PlayerLoginHandler;
import net.cakemc.proxy.protocol.impl.network.handler.PlayerStatusHandler;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientDisconnectPacket;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ClientLoginDisconnectPacket;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.util.UUID;

public class NetworkPlayer extends SimpleChannelInboundHandler<AbstractPacket> implements PlayerConnection {

	private Channel channel;
	private final MinecraftProtocol protocol;

	private final EventManager eventManager;

	private final PlayerIntentionHandler intentionHandler;
	private final PlayerStatusHandler statusHandler;
	private final PlayerLoginHandler loginHandler;
	private final PlayerConfigHandler configHandler;

	private String accountName;
	private UUID playerUUID;
	private PlayerProfile profile;
	private boolean connected;
	private AbstractProtocol.State currentState;

	/**
	 * Instantiates a new Network player.
	 *
	 * @param channel the channel
	 */
	public NetworkPlayer(Channel channel, MinecraftProtocol protocol, EventManager eventManager, boolean onlineMode) {
		this.channel = channel;
		this.protocol = protocol;
		this.eventManager = eventManager;

		this.connected = true;
		this.currentState = AbstractProtocol.State.HANDSHAKE;

		this.intentionHandler = new PlayerIntentionHandler(this, eventManager);
		this.statusHandler = new PlayerStatusHandler(this, eventManager);
		this.loginHandler = new PlayerLoginHandler(this, eventManager, onlineMode);
		this.configHandler = new PlayerConfigHandler(this, eventManager);
	}

	@Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("error");

		ctx.close();
	}

	@Override
	public void packetReceived(AbstractPacket abstractPacket) {
		PlayerPacketReceivedEvent packetReceivedEvent = new PlayerPacketReceivedEvent(this, abstractPacket);

		this.eventManager.call(packetReceivedEvent);

		if (packetReceivedEvent.isCancelled())
			return;

		if (this.currentState == State.GAME)
			return;

		switch (currentState) {
			case HANDSHAKE -> handleIntent(abstractPacket);
			case LOGIN -> handleLogin(abstractPacket);
			case STATUS -> handleStatus(abstractPacket);
			case CONFIG -> handleConfiguration(abstractPacket);
			default -> throw new IllegalArgumentException("Unexpected value: " + getCurrentState());
		}
	}

	@Override public void handleIntent(AbstractPacket abstractPacket) {
		this.intentionHandler.handleIntent(abstractPacket);
	}

	@Override public void handleLogin(AbstractPacket abstractPacket) {
		this.loginHandler.handleLogin(abstractPacket);
	}

	@Override public void handleStatus(AbstractPacket abstractPacket) {
		this.statusHandler.handleStatus(abstractPacket);
	}

	@Override public void handleConfiguration(AbstractPacket abstractPacket) {
		this.configHandler.handleConfiguration(abstractPacket);
	}


	@Override public void sendConfiguration() {
		this.configHandler.sendServerConfiguration();
	}

	public void sendPacket(AbstractPacket abstractPacket) {
		if (channel == null || !channel.isActive())
			return;

		ServerSendPacketEvent serverSendPacketEvent = new ServerSendPacketEvent(this, abstractPacket);
		this.eventManager.call(serverSendPacketEvent);
		if (serverSendPacketEvent.isCancelled())
			return;

		this.channel.writeAndFlush(serverSendPacketEvent.getPacket());
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
		if (!checkChannelUsability())
			return;
		// COMP_DECODER -> COMP_ENCODER -> PACKET ...

		PacketCompressionCodec codec = new PacketCompressionCodec(
			 new ZlibCompression(), true, MinecraftProtocol.DEFAULT_SERVER_THRESHOLD
		);

		channel.pipeline().addBefore(
			 CodecOrder.ENCRYPTION_ENCODER.getHandlerName(),
			 CodecOrder.COMPRESSION_CODEC.getHandlerName(),
			 codec
		);
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
	public void disconnect(BaseComponent reason) {
		if (!checkChannelUsability())
			return;

		switch (this.currentState) {
			case LOGIN -> this.sendPacket(new ClientLoginDisconnectPacket(reason));
			case CONFIG, GAME -> this.sendPacket(new ClientDisconnectPacket(reason));
		}


		this.channel.flush().close().addListener(future -> {
			this.onDisconnect();
			this.connected = false;
		});
	}

	public void onDisconnect() {

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

		this.channel = null;

		eventManager.call(new PlayerDisconnectEvent(this));
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

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	public PlayerProfile getProfile() {
		return profile;
	}

	public void setProfile(PlayerProfile profile) {
		this.profile = profile;
	}

	public MinecraftProtocol getProtocol() {
		return protocol;
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public byte[] getPass() {
		return this.loginHandler.getPass();
	}

	public KeyPair getKeyPair() {
		return this.loginHandler.getKeyPair();
	}

}
