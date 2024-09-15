package net.cakemc.proxy.protocol.impl.packets.client.impl.scoreboard;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType.Collision;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType.Team.Action;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType.Team.Color;
import net.cakemc.mc.lib.game.scoreboard.ScoreboardType.Visibility;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.Arrays;

public class ClientSetPlayerTeamPacket extends AbstractPacket {

	private BaseComponent suffix;
	private boolean friendlyFire;
	private boolean seeFriendlyInvisibles;
	private Visibility nameTagVisibility;
	private Collision collisionRule;
	private Color color;
	private String[] players;
	private String teamName;
	private Action action;
	private BaseComponent displayName;
	private BaseComponent prefix;

	public ClientSetPlayerTeamPacket(String teamName) {
		this.teamName = teamName;
		this.action = Action.REMOVE;

		this.displayName = null;
		this.prefix = null;
		this.suffix = null;
		this.friendlyFire = false;
		this.seeFriendlyInvisibles = false;
		this.nameTagVisibility = null;
		this.collisionRule = null;
		this.color = null;

		this.players = null;
	}

	public ClientSetPlayerTeamPacket(
		 String teamName, BaseComponent displayName, BaseComponent prefix, BaseComponent suffix,
		 boolean friendlyFire, boolean seeFriendlyInvisibles, Visibility nameTagVisibility,
		 Collision collisionRule, Color color
	) {
		this.teamName = teamName;
		this.action = Action.UPDATE;

		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.seeFriendlyInvisibles = seeFriendlyInvisibles;
		this.nameTagVisibility = nameTagVisibility;
		this.collisionRule = collisionRule;
		this.color = color;

		this.players = null;
	}

	public ClientSetPlayerTeamPacket(String teamName, Action action, String[] players) {
		if (action != Action.ADD_PLAYER && action != Action.REMOVE_PLAYER) {
			throw new IllegalArgumentException("(name, action, players) constructor only valid for adding and removing players.");
		}

		this.teamName = teamName;
		this.action = action;

		this.displayName = null;
		this.prefix = null;
		this.suffix = null;
		this.friendlyFire = false;
		this.seeFriendlyInvisibles = false;
		this.nameTagVisibility = null;
		this.collisionRule = null;
		this.color = null;

		this.players = Arrays.copyOf(players, players.length);
	}

	public ClientSetPlayerTeamPacket(
		 String teamName, BaseComponent displayName, BaseComponent prefix, BaseComponent suffix,
		 boolean friendlyFire, boolean seeFriendlyInvisibles, Visibility nameTagVisibility,
		 Collision collisionRule, Color color, String[] players
	) {
		this.teamName = teamName;
		this.action = Action.CREATE;

		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.seeFriendlyInvisibles = seeFriendlyInvisibles;
		this.nameTagVisibility = nameTagVisibility;
		this.collisionRule = collisionRule;
		this.color = color;

		this.players = Arrays.copyOf(players, players.length);
	}

	public ClientSetPlayerTeamPacket(
		 BaseComponent suffix, boolean friendlyFire, boolean seeFriendlyInvisibles, Visibility nameTagVisibility,
		 Collision collisionRule, Color color, String[] players, String teamName, Action action, BaseComponent displayName,
		 BaseComponent prefix
	) {
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.seeFriendlyInvisibles = seeFriendlyInvisibles;
		this.nameTagVisibility = nameTagVisibility;
		this.collisionRule = collisionRule;
		this.color = color;
		this.players = players;
		this.teamName = teamName;
		this.action = action;
		this.displayName = displayName;
		this.prefix = prefix;
	}

	public ClientSetPlayerTeamPacket() {
	}

	public static ClientSetPlayerTeamPacket remove(String teamName) {
		return new ClientSetPlayerTeamPacket(teamName);
	}

	public static ClientSetPlayerTeamPacket update(
		 String teamName, BaseComponent displayName, BaseComponent prefix, BaseComponent suffix,
		 boolean friendlyFire, boolean seeFriendlyInvisibles, Visibility nameTagVisibility,
		 Collision collisionRule, Color color
	) {
		return new ClientSetPlayerTeamPacket(teamName, displayName, prefix, suffix, friendlyFire,
		                                     seeFriendlyInvisibles, nameTagVisibility, collisionRule, color
		);
	}

	public static ClientSetPlayerTeamPacket addPlayer(String teamName, String[] players) {
		return new ClientSetPlayerTeamPacket(teamName, Action.ADD_PLAYER, players);
	}

	public static ClientSetPlayerTeamPacket removePlayer(String teamName, String[] players) {
		return new ClientSetPlayerTeamPacket(teamName, Action.REMOVE_PLAYER, players);
	}

	public static ClientSetPlayerTeamPacket create(
		 String teamName, BaseComponent displayName, BaseComponent prefix, BaseComponent suffix,
		 boolean friendlyFire, boolean seeFriendlyInvisibles, Visibility nameTagVisibility,
		 Collision collisionRule, Color color, String[] players
	) {
		return new ClientSetPlayerTeamPacket(teamName, displayName, prefix, suffix, friendlyFire,
		                                     seeFriendlyInvisibles, nameTagVisibility, collisionRule, color, players
		);
	}

	@Override
	public void read(ByteBuf buf) {
		this.teamName = readString(buf);
		this.action = Action.values()[buf.readByte()];
		if (this.action == Action.CREATE || this.action == Action.UPDATE) {
			this.displayName = readComponent(buf);
			byte flags = buf.readByte();
			this.friendlyFire = (flags & 0x1) != 0;
			this.seeFriendlyInvisibles = (flags & 0x2) != 0;
			this.nameTagVisibility = Visibility.byName(readString(buf));
			this.collisionRule = Collision.byName(readString(buf));

			this.color = Color.values()[readVarInt(buf)];

			this.prefix = readComponent(buf);
			this.suffix = readComponent(buf);
		} else {
			this.displayName = null;
			this.prefix = null;
			this.suffix = null;
			this.friendlyFire = false;
			this.seeFriendlyInvisibles = false;
			this.nameTagVisibility = null;
			this.collisionRule = null;
			this.color = null;
		}

		if (this.action == Action.CREATE || this.action == Action.ADD_PLAYER || this.action == Action.REMOVE_PLAYER) {
			this.players = new String[readVarInt(buf)];
			for (int index = 0 ; index < this.players.length ; index++) {
				this.players[index] = readString(buf);
			}
		} else {
			this.players = null;
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.teamName);
		buf.writeByte(this.action.ordinal());

		byte flag = (byte) ((this.friendlyFire ? 0x1 : 0x0) | (this.seeFriendlyInvisibles ? 0x2 : 0x0));

		switch (this.action) {
			case CREATE -> {
				writeComponent(buf, displayName);
				buf.writeByte(flag);
				writeString(buf, nameTagVisibility.getDisplay());
				writeString(buf, collisionRule.getDisplay());
				writeVarInt(buf, color.ordinal());
				writeComponent(buf, prefix);
				writeComponent(buf, suffix);
				writeVarInt(buf, players.length);
				for (String player : players) {
					writeString(buf, player);
				}
			}
			case REMOVE -> {}
			case UPDATE -> {
				writeComponent(buf, displayName);
				buf.writeByte(flag);
				writeString(buf, nameTagVisibility.getDisplay());
				writeString(buf, collisionRule.getDisplay());
				writeVarInt(buf, color.ordinal());
				writeComponent(buf, prefix);
				writeComponent(buf, suffix);

			}
			case ADD_PLAYER -> {
				writeVarInt(buf, players.length);
				for (String player : players) {
					writeString(buf, player);
				}
			}
			case REMOVE_PLAYER -> {
				writeVarInt(buf, players.length);
				for (String player : players) {
					writeString(buf, player);
				}
			}

		}
	}

	@Override public String toString() {
		return "ClientSetPlayerTeamPacket{" +
		       "suffix=" + suffix +
		       ", friendlyFire=" + friendlyFire +
		       ", seeFriendlyInvisibles=" + seeFriendlyInvisibles +
		       ", nameTagVisibility=" + nameTagVisibility +
		       ", collisionRule=" + collisionRule +
		       ", color=" + color +
		       ", players=" + Arrays.toString(players) +
		       ", teamName='" + teamName + '\'' +
		       ", action=" + action +
		       ", displayName=" + displayName +
		       ", prefix=" + prefix +
		       '}';
	}

	public BaseComponent getSuffix() {
		return suffix;
	}

	public void setSuffix(BaseComponent suffix) {
		this.suffix = suffix;
	}

	public boolean isFriendlyFire() {
		return friendlyFire;
	}

	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public boolean isSeeFriendlyInvisibles() {
		return seeFriendlyInvisibles;
	}

	public void setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
		this.seeFriendlyInvisibles = seeFriendlyInvisibles;
	}

	public Visibility getNameTagVisibility() {
		return nameTagVisibility;
	}

	public void setNameTagVisibility(Visibility nameTagVisibility) {
		this.nameTagVisibility = nameTagVisibility;
	}

	public Collision getCollisionRule() {
		return collisionRule;
	}

	public void setCollisionRule(Collision collisionRule) {
		this.collisionRule = collisionRule;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String[] getPlayers() {
		return players;
	}

	public void setPlayers(String[] players) {
		this.players = players;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public BaseComponent getDisplayName() {
		return displayName;
	}

	public void setDisplayName(BaseComponent displayName) {
		this.displayName = displayName;
	}

	public BaseComponent getPrefix() {
		return prefix;
	}

	public void setPrefix(BaseComponent prefix) {
		this.prefix = prefix;
	}
}

