package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerMovePlayerPosPacket extends AbstractPacket {

	private boolean onGround;
	private double x;
	private double y;
	private double z;

	public ServerMovePlayerPosPacket(boolean onGround, double x, double y, double z) {
		this.onGround = onGround;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public ServerMovePlayerPosPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.onGround = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeDouble(this.x);
		buf.writeDouble(this.y);
		buf.writeDouble(this.z);
		buf.writeBoolean(this.onGround);
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
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
}

