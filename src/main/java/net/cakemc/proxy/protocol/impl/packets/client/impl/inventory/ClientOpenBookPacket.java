package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.AbstractPlayer.Hand;
import net.cakemc.mc.lib.game.entity.player.AbstractPlayer.Hand.Type;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientOpenBookPacket extends AbstractPacket {

	private Type hand;

	public ClientOpenBookPacket(Type hand) {
		this.hand = hand;
	}

	public ClientOpenBookPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		hand = Type.values()[readVarInt(buf)];
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, hand.ordinal());
	}

	public Type getHand() {
		return hand;
	}
}

