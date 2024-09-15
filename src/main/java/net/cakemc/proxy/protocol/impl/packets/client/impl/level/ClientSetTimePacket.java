package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetTimePacket extends AbstractPacket {

	private long serverAge;
	private long gameTime;

	public ClientSetTimePacket(long serverAge, long gameTime) {
		this.serverAge = serverAge;
		this.gameTime = gameTime;
	}

	public ClientSetTimePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.serverAge = buf.readLong();
		this.gameTime = buf.readLong();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeLong(serverAge);
		buf.writeLong(gameTime);
	}


	public long getServerAge() {
		return serverAge;
	}

	public void setServerAge(long serverAge) {
		this.serverAge = serverAge;
	}

	public long getGameTime() {
		return gameTime;
	}

	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}
}

