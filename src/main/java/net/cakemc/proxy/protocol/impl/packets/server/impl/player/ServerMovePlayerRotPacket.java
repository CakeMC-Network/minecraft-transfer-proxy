package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerMovePlayerRotPacket extends AbstractPacket {

	private float yaw;
	private float pitch;
	private boolean onGround;

	public ServerMovePlayerRotPacket(float yaw, float pitch, boolean onGround) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	public ServerMovePlayerRotPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.yaw = buf.readFloat();
		this.pitch = buf.readFloat();
		this.onGround = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeFloat(this.yaw);
		buf.writeFloat(this.pitch);
		buf.writeBoolean(this.onGround);
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
}

