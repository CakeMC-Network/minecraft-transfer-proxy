package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Position;

public class ClientSetDefaultSpawnPositionPacket extends AbstractPacket {

	private Position position;
	private float angle;

	public ClientSetDefaultSpawnPositionPacket(Position position, float angle) {
		this.position = position;
		this.angle = angle;
	}

	public ClientSetDefaultSpawnPositionPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.position = readPosition(buf).toPosition();
		this.angle = buf.readFloat();
	}

	@Override
	public void write(ByteBuf buf) {
		writePosition(buf, position.toWorldPosition());
		buf.writeFloat(angle);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
}

