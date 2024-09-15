package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.sound.SoundCategory;

public class ClientStopSoundPacket extends AbstractPacket {

	private SoundCategory category;
	private String sound;

	public ClientStopSoundPacket(SoundCategory category, String sound) {
		this.category = category;
		this.sound = sound;
	}

	public ClientStopSoundPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		int flags = buf.readByte();
		if ((flags & 0x01) != 0) {
			this.category = SoundCategory.values()[readVarInt(buf)];
		} else {
			this.category = null;
		}

		if ((flags & 0x02) != 0) {
			this.sound = readString(buf);
		} else {
			this.sound = null;
		}
	}

	@Override
	public void write(ByteBuf buf) {
		int flags = 0;
		if (this.category != null) {
			flags |= 0x01;
		}

		if (this.sound != null) {
			flags |= 0x02;
		}

		buf.writeByte(flags);
		if (this.category != null) {
			buf.writeByte(this.category.ordinal());
		}

		if (this.sound != null) {
			writeString(buf, this.sound);
		}
	}

	public SoundCategory getCategory() {
		return category;
	}

	public String getSound() {
		return sound;
	}
}

