package net.cakemc.proxy.protocol.api.protocol;

public abstract class AbstractProtocol {
	public abstract void register();

	public enum State {HANDSHAKE, LOGIN, GAME, STATUS, CONFIG;}

}
