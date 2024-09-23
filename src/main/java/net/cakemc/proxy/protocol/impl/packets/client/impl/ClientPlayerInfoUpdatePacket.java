package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.PlayerProfile.Property;
import net.cakemc.mc.lib.game.entity.player.GameMode;
import net.cakemc.mc.lib.game.entity.player.ListEntry;
import net.cakemc.mc.lib.game.entity.player.ListEntry.Action;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ClientPlayerInfoUpdatePacket extends AbstractPacket {

	private EnumSet<Action> actions;
	private ListEntry[] entries;

	public ClientPlayerInfoUpdatePacket(
		 EnumSet<Action> actions,
		 ListEntry[] entries
	) {
		this.actions = actions;
		this.entries = entries;
	}

	public static List<Property> readProperties(ByteBuf buf) {
		List<Property> properties = new ArrayList<>();
		int size = readVarInt(buf);
		for (int i = 0 ; i < size ; i++) {
			String name = readString(buf);
			String value = readString(buf);
			String signature = "";
			boolean hasSignature = buf.readBoolean();
			if (hasSignature) {
				signature = readString(buf);
			}
			properties.add(new Property(name, value, signature));
		}

		return properties;
	}

	public static void writeProperties(ByteBuf buf, List<Property> properties) {
		writeVarInt(buf, properties.size());
		for (Property property : properties) {
			writeString(buf, property.getName());
			writeString(buf, property.getValue());
			String signature = property.getSignature();
			if (signature != null && !signature.isEmpty()) {
				buf.writeBoolean(true);
				writeString(buf, signature);
			} else {
				buf.writeBoolean(false);
			}
		}
	}

	@Override
	public void read(ByteBuf buf) {
		this.actions = readEnumSet(buf, Action.values());
		this.entries = new ListEntry[readVarInt(buf)];
		for (int count = 0 ; count < this.entries.length ; count++) {
			ListEntry entry = new ListEntry(readUUID(buf));
			for (Action action : this.actions) {
				switch (action) {
					case ADD_PLAYER -> {
						PlayerProfile profile = new PlayerProfile(entry.getProfileId().toString(), readString(buf));

						List<Property> property = readProperties(buf);
						property.forEach(properties -> profile.getProperties().add(properties));
						entry.setProfile(profile);
					}
					case INITIALIZE_CHAT -> {
						if (buf.readBoolean()) {
							entry.setSessionId(readUUID(buf));
							entry.setExpiresAt(buf.readLong());
							byte[] keyBytes = readByteArray(buf);
							entry.setKeySignature(readByteArray(buf));

							PublicKey publicKey;
							try {
								publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
							} catch (GeneralSecurityException e) {
								throw new IllegalStateException("Could not decode public key.", e);
							}

							entry.setPublicKey(publicKey);
						}
					}
					case UPDATE_GAME_MODE -> {
						GameMode gameMode = GameMode.byId(readVarInt(buf));

						entry.setGameMode(gameMode);
					}
					case UPDATE_LISTED -> {
						boolean listed = buf.readBoolean();

						entry.setListed(listed);
					}
					case UPDATE_LATENCY -> {
						int latency = readVarInt(buf);

						entry.setLatency(latency);
					}
					case UPDATE_DISPLAY_NAME -> {
						if (buf.readBoolean()) {
							BaseComponent displayName = readComponent(buf);
							entry.setDisplayName(displayName);
						}

					}
				}
			}

			this.entries[count] = entry;
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeEnumSet(buf, this.actions, Action.values());
		writeVarInt(buf, this.entries.length);
		for (ListEntry entry : this.entries) {
			writeUUID(buf, entry.getProfileId());
			for (Action action : this.actions) {
				switch (action) {
					case ADD_PLAYER -> {
						PlayerProfile profile = entry.getProfile();
						if (profile == null) {
							throw new IllegalArgumentException("Cannot ADD " + entry.getProfileId() + " without a profile.");
						}

						writeString(buf, profile.getName());
						writeProperties(buf, profile.getProperties());
					}
					case INITIALIZE_CHAT -> {
						buf.writeBoolean(entry.getPublicKey() != null);
						if (entry.getPublicKey() != null) {
							writeUUID(buf, entry.getSessionId());
							buf.writeLong(entry.getExpiresAt());
							writeByteArray(buf, entry.getPublicKey().getEncoded());
							writeByteArray(buf, entry.getKeySignature());
						}
					}
					case UPDATE_GAME_MODE -> writeVarInt(buf, entry.getGameMode().ordinal());
					case UPDATE_LISTED -> buf.writeBoolean(entry.isListed());
					case UPDATE_LATENCY -> writeVarInt(buf, entry.getLatency());
					case UPDATE_DISPLAY_NAME -> {
						buf.writeBoolean(entry.getDisplayName() != null);
						if (entry.getDisplayName() != null)
							writeComponent(buf, entry.getDisplayName());
					}
				}
			}
		}
	}

}

