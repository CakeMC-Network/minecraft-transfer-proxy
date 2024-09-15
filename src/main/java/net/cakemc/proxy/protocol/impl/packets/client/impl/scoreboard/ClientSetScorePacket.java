package net.cakemc.proxy.protocol.impl.packets.client.impl.scoreboard;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.text.number.NumberFormat;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientSetScorePacket extends AbstractPacket {

	private String owner;
	private String objective;
	private int value;
	private BaseComponent display;
	private NumberFormat numberFormat;

	public ClientSetScorePacket(String owner, String objective, int value, BaseComponent display, NumberFormat numberFormat) {
		this.owner = owner;
		this.objective = objective;
		this.value = value;
		this.display = display;
		this.numberFormat = numberFormat;
	}

	public ClientSetScorePacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.owner = readString(buf);
		this.objective = readString(buf);
		this.value = readVarInt(buf);

		if (buf.readBoolean())
			display = readComponent(buf);
		if (buf.readBoolean())
			numberFormat = readNumberFormat(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.owner);
		writeString(buf, this.objective);
		writeVarInt(buf, this.value);

		if (this.display == null)
			buf.writeBoolean(false);
		else {
			buf.writeBoolean(true);
			writeComponent(buf, this.display);
		}
		if (this.numberFormat == null)
			buf.writeBoolean(false);
		else {
			buf.writeBoolean(true);
			writeNumberFormat(buf, this.numberFormat);
		}
	}

	@Override public String toString() {
		return "ClientSetScorePacket{" +
		       "owner='" + owner + '\'' +
		       ", objective='" + objective + '\'' +
		       ", value=" + value +
		       ", display=" + display +
		       ", numberFormat=" + numberFormat +
		       '}';
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public BaseComponent getDisplay() {
		return display;
	}

	public void setDisplay(BaseComponent display) {
		this.display = display;
	}

	public NumberFormat getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}
}

