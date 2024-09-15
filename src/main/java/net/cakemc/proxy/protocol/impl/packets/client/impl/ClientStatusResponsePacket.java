package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.common.type.json.Json;
import net.cakemc.mc.lib.common.type.json.JsonArray;
import net.cakemc.mc.lib.common.type.json.JsonObject;
import net.cakemc.mc.lib.common.type.json.JsonValue;
import net.cakemc.mc.lib.game.PlayerProfile;
import net.cakemc.mc.lib.game.Status;
import net.cakemc.mc.lib.game.text.rewrite.Text;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ClientStatusResponsePacket extends AbstractPacket {
	private JsonObject jsonObject;

	private Status.Info info;

	public ClientStatusResponsePacket(Status.Info info) {
		this.info = info;
	}

	public ClientStatusResponsePacket() {
	}

	private static JsonObject toJson(Status.Info info) {
		JsonObject obj = new JsonObject();

		Text component = info.getDescription();

		obj.add("description", component.toJson());

		if (info.getPlayerList() != null) {
			JsonObject plrs = new JsonObject();
			plrs.add("max", info.getPlayerList().getMaxPlayers());
			plrs.add("online", info.getPlayerList().getOnlinePlayers());
			if (!info.getPlayerList().getPlayers().isEmpty()) {
				JsonArray array = new JsonArray();
				for (PlayerProfile profile : info.getPlayerList().getPlayers()) {
					JsonObject o = new JsonObject();
					o.add("name", profile.getName());
					o.add("id", profile.getUuidString());
					array.add(o);
				}

				plrs.add("sample", array);
			}
			obj.add("players", plrs);
		}

		if (info.getVersionInfo() != null) {
			JsonObject ver = new JsonObject();
			ver.add("name", info.getVersionInfo().getVersionName());
			ver.add("protocol", info.getVersionInfo().getProtocolVersion());
			obj.add("version", ver);
		}

		if (info.getIconPng() != null) {
			obj.add("favicon", iconToString(info.getIconPng()));
		}

		obj.add("enforcesSecureChat", info.isEnforcesSecureChat());

		return obj;
	}

	public static byte[] stringToIcon(String str) {
		if (str.startsWith("data:image/png;base64,")) {
			str = str.substring("data:image/png;base64,".length());
		}

		return Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
	}

	public static String iconToString(byte[] icon) {
		return "data:image/png;base64," + new String(Base64.getEncoder().encode(icon), StandardCharsets.UTF_8);
	}

	@Override
	public void read(ByteBuf buf) {
		String json = readString(buf);
		Json.object();
		this.jsonObject = JsonObject.readFrom(json);
		this.info = this.parseInfo();
	}

	public Status.Info parseInfo() {
		JsonValue desc = jsonObject.get("description");
		Text description = Text.fromJson(desc.asObject());

		Status.PlayerList players = null;
		if (jsonObject.get("players") != null) {
			JsonObject plrs = jsonObject.get("players").asObject();
			List<PlayerProfile> profiles = new ArrayList<>();
			if (plrs.get("sample") != null) {
				JsonArray prof = plrs.get("sample").asArray();
				if (!prof.isEmpty()) {
					for (int index = 0 ; index < prof.size() ; index++) {
						JsonObject o = prof.get(index).asObject();
						profiles.add(new PlayerProfile(o.get("id").asString(), o.get("name").asString()));
					}
				}
			}

			players = new Status.PlayerList(plrs.get("max").asInt(), plrs.get("online").asInt(), profiles);
		}

		Status.Version version = null;
		if (jsonObject.get("version") != null) {
			JsonObject ver = jsonObject.get("version").asObject();
			version = new Status.Version(ver.get("name").asString(), ver.get("protocol").asInt());
		}

		byte[] icon = null;
		if (jsonObject.get("favicon") != null) {
			icon = stringToIcon(jsonObject.get("favicon").asString());
		}

		boolean enforcesSecureChat = false;
		if (jsonObject.get("enforcesSecureChat") != null) {
			enforcesSecureChat = jsonObject.get("enforcesSecureChat").asBoolean();
		}

		return new Status.Info(description, players, version, icon, enforcesSecureChat);
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, toJson(this.info).toString());
	}

	public JsonObject getJsonObject() {
		return jsonObject;
	}

	public Status.Info getInfo() {
		return info;
	}

	@Override
	public String toString() {
		return "ClientStatusResponsePacket{" +
		       "jsonObject=" + jsonObject +
		       ", info=" + info +
		       '}';
	}
}
