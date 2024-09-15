package net.cakemc.proxy.protocol.impl.packets.client.impl.player;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Position;

public class ClientPlayerPositionPacket extends AbstractPacket {

	private Position position;
	private int teleportId;
	private int flags;

	public ClientPlayerPositionPacket(Position position, int teleportId, int flags) {
		this.position = position;
		this.teleportId = teleportId;
		this.flags = flags;
	}

	public ClientPlayerPositionPacket(Position position) {
		this.position = position;

		teleportId = 0;
		flags = 0;
	}

	public ClientPlayerPositionPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.position = new Position(
			 buf.readDouble(),
			 buf.readDouble(),
			 buf.readDouble(),
			 buf.readFloat(),
			 buf.readFloat()
		);

		this.flags = buf.readUnsignedByte();
		this.teleportId = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeDouble(position.getX());
		buf.writeDouble(position.getY());
		buf.writeDouble(position.getZ());
		buf.writeFloat(position.getYaw());
		buf.writeFloat(position.getPitch());

		buf.writeByte(flags);
		writeVarInt(buf, teleportId);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getTeleportId() {
		return teleportId;
	}

	public void setTeleportId(int teleportId) {
		this.teleportId = teleportId;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}
}

