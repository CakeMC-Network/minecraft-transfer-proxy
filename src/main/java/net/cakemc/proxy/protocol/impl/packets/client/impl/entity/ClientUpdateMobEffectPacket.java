package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.Effect;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientUpdateMobEffectPacket extends AbstractPacket {

	private static final int FLAG_AMBIENT = 0x01;
	private static final int FLAG_SHOW_PARTICLES = 0x02;
	private static final int FLAG_SHOW_ICON = 0x04;
	private static final int FLAG_BLEND = 0x08;

	private int entityId;
	private Effect effect;
	private int amplifier;
	private int duration;
	private boolean ambient;
	private boolean showParticles;
	private boolean showIcon;
	private boolean blend;

	public ClientUpdateMobEffectPacket(
		 int entityId, Effect effect, int amplifier, int duration,
		 boolean ambient, boolean showParticles, boolean showIcon, boolean blend
	) {
		this.entityId = entityId;
		this.effect = effect;
		this.amplifier = amplifier;
		this.duration = duration;
		this.ambient = ambient;
		this.showParticles = showParticles;
		this.showIcon = showIcon;
		this.blend = blend;
	}

	public ClientUpdateMobEffectPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.effect = readEffect(buf);
		this.amplifier = readVarInt(buf);
		this.duration = readVarInt(buf);

		int flags = buf.readByte();
		this.ambient = (flags & FLAG_AMBIENT) != 0;
		this.showParticles = (flags & FLAG_SHOW_PARTICLES) != 0;
		this.showIcon = (flags & FLAG_SHOW_ICON) != 0;
		this.blend = (flags & FLAG_BLEND) != 0;
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		writeEffect(buf, this.effect);
		writeVarInt(buf, this.amplifier);
		writeVarInt(buf, this.duration);

		int flags = 0;
		if (this.ambient) {
			flags |= FLAG_AMBIENT;
		}
		if (this.showParticles) {
			flags |= FLAG_SHOW_PARTICLES;
		}
		if (this.showIcon) {
			flags |= FLAG_SHOW_ICON;
		}
		if (this.blend) {
			flags |= FLAG_BLEND;
		}

		buf.writeByte(flags);
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isAmbient() {
		return ambient;
	}

	public void setAmbient(boolean ambient) {
		this.ambient = ambient;
	}

	public boolean isShowParticles() {
		return showParticles;
	}

	public void setShowParticles(boolean showParticles) {
		this.showParticles = showParticles;
	}

	public boolean isShowIcon() {
		return showIcon;
	}

	public void setShowIcon(boolean showIcon) {
		this.showIcon = showIcon;
	}

	public boolean isBlend() {
		return blend;
	}

	public void setBlend(boolean blend) {
		this.blend = blend;
	}
}

