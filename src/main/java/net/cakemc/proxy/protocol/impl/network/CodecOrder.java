package net.cakemc.proxy.protocol.impl.network;

/**
 * The enum Codec order.
 */
public enum CodecOrder {

	/**
	 * Framing decoder codec order.
	 */
	FRAMING_DECODER("framing_decoder"),
	/**
	 * Framing encoder codec order.
	 */
	FRAMING_ENCODER("framing_encoder"),

	/**
	 * Compression decoder codec order.
	 */
	COMPRESSION_CODEC("compression_codec"),

	/**
	 * Encryption encoder codec order.
	 */
	ENCRYPTION_ENCODER("decryption"),
	/**
	 * Decryption decoder codec order.
	 */
	DECRYPTION_DECODER("encryption"),

	/**
	 * Minecraft decoder codec order.
	 */
	MINECRAFT_DECODER("minecraft_decoder"),
	/**
	 * Minecraft encoder codec order.
	 */
	MINECRAFT_ENCODER("minecraft_encoder"),

	/**
	 * Read timeout handler codec order.
	 */
	// sub
	READ_TIMEOUT_HANDLER("read_timeout_handler"),
	/**
	 * Write timeout handler codec order.
	 */
	WRITE_TIMEOUT_HANDLER("write_timeout_handler"),

	/**
	 * Network player codec order.
	 */
	NETWORK_PLAYER("manager"),
	;

	private final String handlerName;

	CodecOrder(String handlerName) {
		this.handlerName = handlerName;
	}

	/**
	 * Gets handler name.
	 *
	 * @return the handler name
	 */
	public String getHandlerName() {
		return handlerName;
	}
}
