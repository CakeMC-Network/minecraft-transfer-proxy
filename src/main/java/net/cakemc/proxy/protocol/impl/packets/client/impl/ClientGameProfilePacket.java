package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.PlayerProfile.Property;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientGameProfilePacket extends AbstractPacket {

	private UUID uuid;
	private PlayerProfile user;

	private boolean errorHandle = false;

	public ClientGameProfilePacket(UUID uuid, PlayerProfile user) {
		this.uuid = uuid;
		this.user = user;
	}

	public ClientGameProfilePacket() {
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
		this.uuid = readUUID(buf);
		String name = readString(buf);
		List<Property> properties = readProperties(buf);
		errorHandle = buf.readBoolean();

		this.user = new PlayerProfile(uuid.toString(), name);
		user.getProperties().addAll(properties);
	}

	@Override
	public void write(ByteBuf buf) {
		writeUUID(buf, uuid);
		writeString(buf, user.getName());
		writeProperties(buf, user.getProperties());

		buf.writeBoolean(errorHandle);
	}

	public PlayerProfile getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "ClientGameProfilePacket{" +
		       "uuid=" + uuid +
		       ", user=" + user +
		       ", errorHandle=" + errorHandle +
		       '}';
	}

	public UUID getUuid() {
		return uuid;
	}
}

