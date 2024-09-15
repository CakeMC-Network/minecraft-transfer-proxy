package net.cakemc.proxy.protocol.impl.packets.client.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetHealthPacket extends AbstractPacket {

	private float health;
	private int food;
	private float saturation;

	public ClientSetHealthPacket(float health, int food, float saturation) {
		this.health = health;
		this.food = food;
		this.saturation = saturation;
	}

	public ClientSetHealthPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.health = buf.readFloat();
		this.food = readVarInt(buf);
		this.saturation = buf.readFloat();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeFloat(health);
		writeVarInt(buf, food);
		buf.writeFloat(saturation);
	}

	public float getHealth() {
		return health;
	}

	public float getSaturation() {
		return saturation;
	}

	public int getFood() {
		return food;
	}
}

