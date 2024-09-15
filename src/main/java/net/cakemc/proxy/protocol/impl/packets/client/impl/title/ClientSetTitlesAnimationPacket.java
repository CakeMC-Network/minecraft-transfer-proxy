package net.cakemc.proxy.protocol.impl.packets.client.impl.title;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetTitlesAnimationPacket extends AbstractPacket {

	private int fadeIn;
	private int stay;
	private int fadeOut;

	public ClientSetTitlesAnimationPacket() {
	}

	public ClientSetTitlesAnimationPacket(int fadeIn, int stay, int fadeOut) {
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
	}


	@Override
	public void read(ByteBuf buf) {
		this.fadeIn = buf.readInt();
		this.stay = buf.readInt();
		this.fadeOut = buf.readInt();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeInt(this.fadeIn);
		buf.writeInt(this.stay);
		buf.writeInt(this.fadeOut);
	}

	public int getFadeIn() {
		return fadeIn;
	}

	public void setFadeIn(int fadeIn) {
		this.fadeIn = fadeIn;
	}

	public int getStay() {
		return stay;
	}

	public void setStay(int stay) {
		this.stay = stay;
	}

	public int getFadeOut() {
		return fadeOut;
	}

	public void setFadeOut(int fadeOut) {
		this.fadeOut = fadeOut;
	}
}

