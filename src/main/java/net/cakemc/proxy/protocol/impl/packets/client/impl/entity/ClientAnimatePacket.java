package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.Animation;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientAnimatePacket extends AbstractPacket {

	private int entityId;
	private Animation animation;

	public ClientAnimatePacket(int entityId, Animation animation) {
		this.entityId = entityId;
		this.animation = animation;
	}

	public ClientAnimatePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		entityId = readVarInt(buf);
		animation = Animation.byId(buf.readByte());
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, entityId);
		buf.writeByte(animation == null ? -1 : animation.getId());
	}

	public int getEntityId() {
		return entityId;
	}

	public Animation getAnimation() {
		return animation;
	}
}

