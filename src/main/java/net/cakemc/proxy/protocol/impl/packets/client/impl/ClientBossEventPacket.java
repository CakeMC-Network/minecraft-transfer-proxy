package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.BossBar;
import net.cakemc.mc.lib.game.BossBar.Action;
import net.cakemc.mc.lib.game.BossBar.Color;
import net.cakemc.mc.lib.game.BossBar.Division;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.UUID;

public class ClientBossEventPacket extends AbstractPacket {

	private UUID uuid;
	private Action action;

	private BaseComponent title;

	private float health;

	private Color color;
	private Division division;

	private boolean darkenSky;
	private boolean playEndMusic;
	private boolean showFog;

	public ClientBossEventPacket(
		 UUID uuid, Action action, BaseComponent title, float health, Color color,
		 Division division, boolean darkenSky, boolean playEndMusic, boolean showFog
	) {
		this.uuid = uuid;
		this.action = action;
		this.title = title;
		this.health = health;
		this.color = color;
		this.division = division;
		this.darkenSky = darkenSky;
		this.playEndMusic = playEndMusic;
		this.showFog = showFog;
	}

	@Override
	public void read(ByteBuf buf) {
		this.uuid = readUUID(buf);
		this.action = Action.values()[readVarInt(buf)];

		if (this.action == Action.ADD || this.action == Action.UPDATE_TITLE) {
			this.title = readComponent(buf);
		} else {
			this.title = null;
		}

		if (this.action == Action.ADD || this.action == Action.UPDATE_HEALTH) {
			this.health = buf.readFloat();
		} else {
			this.health = 0f;
		}

		if (this.action == Action.ADD || this.action == Action.UPDATE_STYLE) {
			this.color = Color.values()[readVarInt(buf)];
			this.division = Division.values()[readVarInt(buf)];
		} else {
			this.color = null;
			this.division = null;
		}

		if (this.action == Action.ADD || this.action == Action.UPDATE_FLAGS) {
			int flags = buf.readUnsignedByte();
			this.darkenSky = (flags & 0x1) == 0x1;
			this.playEndMusic = (flags & 0x2) == 0x2;
			this.showFog = (flags & 0x4) == 0x4;
		} else {
			this.darkenSky = false;
			this.playEndMusic = false;
			this.showFog = false;
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeUUID(buf, this.uuid);
		writeVarInt(buf, this.action.ordinal());

		if (this.action == Action.ADD || this.action == Action.UPDATE_TITLE) {
			writeComponent(buf, this.title);
		}

		if (this.action == Action.ADD || this.action == Action.UPDATE_HEALTH) {
			buf.writeFloat(this.health);
		}

		if (this.action == Action.ADD || this.action == Action.UPDATE_STYLE) {
			writeVarInt(buf, this.color.ordinal());
			writeVarInt(buf, this.division.ordinal());
		}

		if (this.action == Action.ADD || this.action == Action.UPDATE_FLAGS) {
			int flags = 0;
			if (this.darkenSky) {
				flags |= 0x1;
			}

			if (this.playEndMusic) {
				flags |= 0x2;
			}

			if (this.showFog) {
				flags |= 0x4;
			}

			buf.writeByte(flags);
		}
	}

}

