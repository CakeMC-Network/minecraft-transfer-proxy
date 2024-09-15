package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.sound.CustomSound;
import net.cakemc.mc.lib.world.sound.GameSound;
import net.cakemc.mc.lib.world.sound.Sound;
import net.cakemc.mc.lib.world.sound.SoundCategory;

public class ClientSoundEntityPacket extends AbstractPacket {

	private Sound sound;
	private SoundCategory category;
	private int entityId;
	private float volume;
	private float pitch;
	private long seed;

	public ClientSoundEntityPacket() {
	}

	public ClientSoundEntityPacket(Sound sound, SoundCategory category, int entityId, float volume, float pitch, long seed) {
		this.sound = sound;
		this.category = category;
		this.entityId = entityId;
		this.volume = volume;
		this.pitch = pitch;
		this.seed = seed;
	}

	@Override
	public void read(ByteBuf buf) {
		this.sound = readById(buf, value -> {
			return GameSound.values()[value];
		}, AbstractPacket::readSoundEvent);

		this.category = SoundCategory.values()[readVarInt(buf)];
		this.entityId = readVarInt(buf);
		this.volume = buf.readFloat();
		this.pitch = buf.readFloat();
		this.seed = buf.readLong();
	}

	@Override
	public void write(ByteBuf buf) {
		if (this.sound instanceof CustomSound) {
			writeVarInt(buf, 0);
			writeSoundEvent(buf, this.sound);
		} else {
			writeVarInt(buf, ((GameSound) this.sound).ordinal() + 1);
		}
		writeVarInt(buf, category.ordinal());
		writeVarInt(buf, this.entityId);
		buf.writeFloat(this.volume);
		buf.writeFloat(this.pitch);
		buf.writeLong(this.seed);
	}


	public Sound getSound() {
		return sound;
	}

	public SoundCategory getCategory() {
		return category;
	}

	public int getEntityId() {
		return entityId;
	}

	public float getVolume() {
		return volume;
	}

	public float getPitch() {
		return pitch;
	}

	public long getSeed() {
		return seed;
	}
}

