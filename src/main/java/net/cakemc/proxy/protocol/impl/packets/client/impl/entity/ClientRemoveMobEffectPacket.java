package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.Effect;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientRemoveMobEffectPacket extends AbstractPacket {

	private int entityId;
	private Effect effect;

	public ClientRemoveMobEffectPacket() {
	}

	public ClientRemoveMobEffectPacket(int entityId, Effect effect) {
		this.entityId = entityId;
		this.effect = effect;
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.effect = readEffect(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		writeVarInt(buf, this.effect.ordinal());
	}

	public Effect getEffect() {
		return effect;
	}

	public int getEntityId() {
		return entityId;
	}
}

