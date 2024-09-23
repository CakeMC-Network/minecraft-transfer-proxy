package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.Hand.Type;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerUseItemPacket extends AbstractPacket {

	private Type hand;
	private int sequence;
	private float yRot;
	private float xRot;

	public ServerUseItemPacket(Type hand, int sequence, float yRot, float xRot) {
		this.hand = hand;
		this.sequence = sequence;
		this.yRot = yRot;
		this.xRot = xRot;
	}

	public ServerUseItemPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.hand = Type.values()[readVarInt(buf)];
		this.sequence = readVarInt(buf);
		this.yRot = buf.readFloat();
		this.xRot = buf.readFloat();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, hand.ordinal());
		writeVarInt(buf, sequence);
		buf.writeFloat(yRot);
		buf.writeFloat(xRot);
	}

	public int getSequence() {
		return sequence;
	}

	public Type getHand() {
		return hand;
	}

	public float getxRot() {
		return xRot;
	}

	public float getyRot() {
		return yRot;
	}
}

