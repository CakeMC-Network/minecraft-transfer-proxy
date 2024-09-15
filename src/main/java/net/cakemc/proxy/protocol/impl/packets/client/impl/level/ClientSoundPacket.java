package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.sound.CustomSound;
import net.cakemc.mc.lib.world.sound.GameSound;
import net.cakemc.mc.lib.world.sound.Sound;
import net.cakemc.mc.lib.world.sound.SoundCategory;

public class ClientSoundPacket extends AbstractPacket {

	private Sound sound;
	private SoundCategory category;
	private double x;
	private double y;
	private double z;
	private float volume;
	private float pitch;
	private long seed;

	public ClientSoundPacket(Sound sound, SoundCategory category, double x, double y, double z, float volume, float pitch, long seed) {
		this.sound = sound;
		this.category = category;
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.pitch = pitch;
		this.seed = seed;
	}

	public ClientSoundPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.sound = readById(buf, value -> {
			return GameSound.values()[value];
		}, AbstractPacket::readSoundEvent);

		this.category = SoundCategory.values()[readVarInt(buf)];
		this.x = buf.readInt() / 8D;
		this.y = buf.readInt() / 8D;
		this.z = buf.readInt() / 8D;
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
		buf.writeInt((int) (this.x * 8));
		buf.writeInt((int) (this.y * 8));
		buf.writeInt((int) (this.z * 8));
		buf.writeFloat(this.volume);
		buf.writeFloat(this.pitch);
		buf.writeLong(this.seed);
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public SoundCategory getCategory() {
		return category;
	}

	public void setCategory(SoundCategory category) {
		this.category = category;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
}

