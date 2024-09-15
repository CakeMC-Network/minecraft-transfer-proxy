package net.cakemc.proxy.protocol.impl.packets.client.impl.scoreboard;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType.Position;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetDisplayObjectivePacket extends AbstractPacket {

	private Position position;
	private String name;

	public ClientSetDisplayObjectivePacket(Position position, String name) {
		this.position = position;
		this.name = name;
	}

	public ClientSetDisplayObjectivePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.position = Position.values()[readVarInt(buf)];
		this.name = readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, position.ordinal());
		writeString(buf, this.name);
	}

	public String getName() {
		return name;
	}

	public Position getPosition() {
		return position;
	}
}

