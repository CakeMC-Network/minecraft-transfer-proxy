package net.cakemc.proxy.protocol.impl.events.impl.packet;

import net.cakemc.mc.lib.game.event.Cancelable;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.impl.events.NetworkPlayerEvent;
import net.cakemc.proxy.protocol.impl.network.NetworkPlayer;

import java.util.Objects;

public class ServerSendPacketEvent extends NetworkPlayerEvent implements Cancelable {

	private AbstractPacket packet;
	private boolean cancel;

	public ServerSendPacketEvent(NetworkPlayer networkPlayer, AbstractPacket packet) {
		super(networkPlayer);
		this.packet = packet;
	}

	public AbstractPacket getPacket() {
		return packet;
	}

	public void setPacket(AbstractPacket packet) {
		this.packet = packet;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;
		ServerSendPacketEvent that = (ServerSendPacketEvent) o;
		return Objects.equals(packet, that.packet);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), packet);
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public boolean setCancelState(boolean state) {
		return cancel = state;
	}
}
