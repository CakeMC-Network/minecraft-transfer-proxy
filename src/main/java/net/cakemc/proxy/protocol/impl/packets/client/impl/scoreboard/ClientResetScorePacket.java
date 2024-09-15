package net.cakemc.proxy.protocol.impl.packets.client.impl.scoreboard;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientResetScorePacket extends AbstractPacket {

	private String owner;
	private String objective;

	public ClientResetScorePacket(String owner, String objective) {
		this.owner = owner;
		this.objective = objective;
	}

	public ClientResetScorePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		owner = readString(buf);
		if (buf.readBoolean())
			objective = readString(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, owner);
		buf.writeBoolean(objective != null);
		if (objective != null)
			writeString(buf, objective);
	}

	public String getObjective() {
		return objective;
	}

	public String getOwner() {
		return owner;
	}
}

