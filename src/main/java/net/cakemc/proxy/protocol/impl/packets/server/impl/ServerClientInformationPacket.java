package net.cakemc.proxy.protocol.impl.packets.server.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.Hand;
import net.cakemc.mc.lib.game.entity.player.Settings;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

public class ServerClientInformationPacket extends AbstractPacket {

	private String language;
	private int renderDistance;
	private Settings.ChatVisibility chatVisibility;
	private boolean chatColors;
	private List<Settings.SkinPart> skinParts;
	private Hand.Preference handPreference;
	private boolean textFiltering;
	private boolean allowsListing;

	public ServerClientInformationPacket(
		 String language, int renderDistance,
		 Settings.ChatVisibility chatVisibility, boolean chatColors,
		 List<Settings.SkinPart> skinParts, Hand.Preference handPreference,
		 boolean textFiltering, boolean allowsListing
	) {
		this.language = language;
		this.renderDistance = renderDistance;
		this.chatVisibility = chatVisibility;
		this.chatColors = chatColors;
		this.skinParts = skinParts;
		this.handPreference = handPreference;
		this.textFiltering = textFiltering;
		this.allowsListing = allowsListing;
	}

	public ServerClientInformationPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.language = readString(buf);
		this.renderDistance = buf.readByte();
		this.chatVisibility = Settings.ChatVisibility.values()[readVarInt(buf)];
		this.chatColors = buf.readBoolean();
		this.skinParts = new ArrayList<>();

		int skinFlags = buf.readUnsignedByte();
		for (Settings.SkinPart value : Settings.SkinPart
			 .values()) {
			int bit = 1 << value.ordinal();
			if ((skinFlags & bit) == bit) {
				skinParts.add(value);
			}
		}

		this.handPreference = Hand.Preference.values()[readVarInt(buf)];
		textFiltering = buf.readBoolean();
		allowsListing = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, language);
		buf.writeByte(this.renderDistance);
		writeVarInt(buf, chatVisibility.ordinal());
		buf.writeBoolean(chatColors);

		int flags = 0;
		for (Settings.SkinPart skinPart : this.skinParts) {
			flags |= 1 << skinPart.ordinal();
		}

		buf.writeByte(flags);
		writeVarInt(buf, handPreference.ordinal());
		buf.writeBoolean(textFiltering);
		buf.writeBoolean(allowsListing);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getRenderDistance() {
		return renderDistance;
	}

	public void setRenderDistance(int renderDistance) {
		this.renderDistance = renderDistance;
	}

	public Settings.ChatVisibility getChatVisibility() {
		return chatVisibility;
	}

	public void setChatVisibility(Settings.ChatVisibility chatVisibility) {
		this.chatVisibility = chatVisibility;
	}

	public boolean isChatColors() {
		return chatColors;
	}

	public void setChatColors(boolean chatColors) {
		this.chatColors = chatColors;
	}

	public List<Settings.SkinPart> getSkinParts() {
		return skinParts;
	}

	public void setSkinParts(List<Settings.SkinPart> skinParts) {
		this.skinParts = skinParts;
	}

	public Hand.Preference getHandPreference() {
		return handPreference;
	}

	public void setHandPreference(Hand.Preference handPreference) {
		this.handPreference = handPreference;
	}

	public boolean isTextFiltering() {
		return textFiltering;
	}

	public void setTextFiltering(boolean textFiltering) {
		this.textFiltering = textFiltering;
	}

	public boolean isAllowsListing() {
		return allowsListing;
	}

	public void setAllowsListing(boolean allowsListing) {
		this.allowsListing = allowsListing;
	}

	@Override
	public String toString() {
		return "ServerClientInformationPacket{" +
		       "language='" + language + '\'' +
		       ", renderDistance=" + renderDistance +
		       ", chatVisibility=" + chatVisibility +
		       ", chatColors=" + chatColors +
		       ", skinParts=" + skinParts +
		       ", handPreference=" + handPreference +
		       ", textFiltering=" + textFiltering +
		       ", allowsListing=" + allowsListing +
		       '}';
	}
}
