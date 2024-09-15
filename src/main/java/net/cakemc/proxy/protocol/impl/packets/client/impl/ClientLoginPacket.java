package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.AbstractPlayer.GameMode;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.NamedPosition;

import java.util.Arrays;

public class ClientLoginPacket extends AbstractPacket {

	private int entityId;
	private boolean hardcore;
	private String[] worldNames;
	private int maxPlayers;
	private int viewDistance;
	private int simulationDistance;
	private boolean reducedDebugInfo;
	private boolean enableRespawnScreen;
	private boolean doLimitedCrafting;
	private int dimension;
	private String worldName;
	private long hashedSeed;
	private GameMode gameMode;
	private GameMode previousGamemode;
	private boolean debug;
	private boolean flat;
	private boolean hasDeathLocation;
	private NamedPosition lastDeathPos;
	private int portalCooldown;
	private boolean enforcesSecureChat;

	public ClientLoginPacket(
		 int entityId, boolean hardcore, String[] worldNames, int maxPlayers, int viewDistance, int simulationDistance,
		 boolean reducedDebugInfo, boolean enableRespawnScreen, boolean doLimitedCrafting, int dimension, String worldName,
		 long hashedSeed, GameMode gameMode, GameMode previousGamemode, boolean debug, boolean flat, int portalCooldown, boolean enforcesSecureChat
	) {
		this.entityId = entityId;
		this.hardcore = hardcore;
		this.worldNames = worldNames;
		this.maxPlayers = maxPlayers;
		this.viewDistance = viewDistance;
		this.simulationDistance = simulationDistance;
		this.reducedDebugInfo = reducedDebugInfo;
		this.enableRespawnScreen = enableRespawnScreen;
		this.doLimitedCrafting = doLimitedCrafting;
		this.dimension = dimension;
		this.worldName = worldName;
		this.hashedSeed = hashedSeed;
		this.gameMode = gameMode;
		this.previousGamemode = previousGamemode;
		this.debug = debug;
		this.flat = flat;
		this.lastDeathPos = null;
		this.hasDeathLocation = false;
		this.portalCooldown = portalCooldown;
		this.enforcesSecureChat = enforcesSecureChat;
	}

	public ClientLoginPacket(
		 int entityId, boolean hardcore, String[] worldNames, int maxPlayers, int viewDistance,
		 int simulationDistance, boolean reducedDebugInfo, boolean enableRespawnScreen, boolean doLimitedCrafting,
		 int dimension, String worldName, long hashedSeed, GameMode gameMode, GameMode previousGamemode, boolean debug,
		 boolean flat, boolean hasDeathLocation, NamedPosition lastDeathPos, int portalCooldown, boolean enforcesSecureChat
	) {
		this.entityId = entityId;
		this.hardcore = hardcore;
		this.worldNames = worldNames;
		this.maxPlayers = maxPlayers;
		this.viewDistance = viewDistance;
		this.simulationDistance = simulationDistance;
		this.reducedDebugInfo = reducedDebugInfo;
		this.enableRespawnScreen = enableRespawnScreen;
		this.doLimitedCrafting = doLimitedCrafting;
		this.dimension = dimension;
		this.worldName = worldName;
		this.hashedSeed = hashedSeed;
		this.gameMode = gameMode;
		this.previousGamemode = previousGamemode;
		this.debug = debug;
		this.flat = flat;
		this.hasDeathLocation = hasDeathLocation;
		this.lastDeathPos = lastDeathPos;
		this.portalCooldown = portalCooldown;
		this.enforcesSecureChat = enforcesSecureChat;
	}

	public ClientLoginPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.hardcore = buf.readBoolean();

		int worldCount = readVarInt(buf);
		this.worldNames = new String[worldCount];
		for (int i = 0 ; i < worldCount ; i++) {
			this.worldNames[i] = readString(buf);
		}

		this.maxPlayers = readVarInt(buf);
		this.viewDistance = readVarInt(buf);
		this.simulationDistance = readVarInt(buf);
		this.reducedDebugInfo = buf.readBoolean();
		this.enableRespawnScreen = buf.readBoolean();
		this.doLimitedCrafting = buf.readBoolean();
		this.dimension = readVarInt(buf);
		this.worldName = readString(buf);
		this.hashedSeed = buf.readLong();
		this.gameMode = GameMode.byId(buf.readByte());
		this.previousGamemode = GameMode.byId(buf.readByte());
		this.debug = buf.readBoolean();
		this.flat = buf.readBoolean();
		this.hasDeathLocation = buf.readBoolean();

		if (hasDeathLocation)
			this.lastDeathPos = readNamedPosition(buf);

		this.portalCooldown = readVarInt(buf);
		this.enforcesSecureChat = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeBoolean(this.hardcore);

		writeVarInt(buf, this.worldNames.length);
		for (String worldName : this.worldNames) {
			writeString(buf, worldName);
		}

		writeVarInt(buf, this.maxPlayers);
		writeVarInt(buf, this.viewDistance);
		writeVarInt(buf, this.simulationDistance);
		buf.writeBoolean(this.reducedDebugInfo);
		buf.writeBoolean(this.enableRespawnScreen);
		buf.writeBoolean(this.doLimitedCrafting);
		writeVarInt(buf, this.dimension);
		writeString(buf, this.worldName);
		buf.writeLong(this.hashedSeed);
		buf.writeByte(this.gameMode.ordinal());
		buf.ensureWritable(this.previousGamemode.ordinal());
		buf.writeByte(this.previousGamemode.ordinal());
		buf.writeBoolean(this.debug);
		buf.writeBoolean(this.flat);

		buf.writeBoolean(hasDeathLocation);
		if (hasDeathLocation)
			writeNamedPosition(buf, this.lastDeathPos);

		writeVarInt(buf, this.portalCooldown);
		buf.writeBoolean(this.enforcesSecureChat);
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public boolean isHardcore() {
		return hardcore;
	}

	public void setHardcore(boolean hardcore) {
		this.hardcore = hardcore;
	}

	public String[] getWorldNames() {
		return worldNames;
	}

	public void setWorldNames(String[] worldNames) {
		this.worldNames = worldNames;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getViewDistance() {
		return viewDistance;
	}

	public void setViewDistance(int viewDistance) {
		this.viewDistance = viewDistance;
	}

	public int getSimulationDistance() {
		return simulationDistance;
	}

	public void setSimulationDistance(int simulationDistance) {
		this.simulationDistance = simulationDistance;
	}

	public boolean isReducedDebugInfo() {
		return reducedDebugInfo;
	}

	public void setReducedDebugInfo(boolean reducedDebugInfo) {
		this.reducedDebugInfo = reducedDebugInfo;
	}

	public boolean isEnableRespawnScreen() {
		return enableRespawnScreen;
	}

	public void setEnableRespawnScreen(boolean enableRespawnScreen) {
		this.enableRespawnScreen = enableRespawnScreen;
	}

	public boolean isDoLimitedCrafting() {
		return doLimitedCrafting;
	}

	public void setDoLimitedCrafting(boolean doLimitedCrafting) {
		this.doLimitedCrafting = doLimitedCrafting;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public long getHashedSeed() {
		return hashedSeed;
	}

	public void setHashedSeed(long hashedSeed) {
		this.hashedSeed = hashedSeed;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public GameMode getPreviousGamemode() {
		return previousGamemode;
	}

	public void setPreviousGamemode(GameMode previousGamemode) {
		this.previousGamemode = previousGamemode;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isFlat() {
		return flat;
	}

	public void setFlat(boolean flat) {
		this.flat = flat;
	}

	public boolean isHasDeathLocation() {
		return hasDeathLocation;
	}

	public void setHasDeathLocation(boolean hasDeathLocation) {
		this.hasDeathLocation = hasDeathLocation;
	}

	public NamedPosition getLastDeathPos() {
		return lastDeathPos;
	}

	public void setLastDeathPos(NamedPosition lastDeathPos) {
		this.lastDeathPos = lastDeathPos;
	}

	public int getPortalCooldown() {
		return portalCooldown;
	}

	public void setPortalCooldown(int portalCooldown) {
		this.portalCooldown = portalCooldown;
	}

	public boolean isEnforcesSecureChat() {
		return enforcesSecureChat;
	}

	public void setEnforcesSecureChat(boolean enforcesSecureChat) {
		this.enforcesSecureChat = enforcesSecureChat;
	}

	@Override public String toString() {
		return "ClientLoginPacket{" +
		       "entityId=" + entityId +
		       ", hardcore=" + hardcore +
		       ", worldNames=" + Arrays.toString(worldNames) +
		       ", maxPlayers=" + maxPlayers +
		       ", viewDistance=" + viewDistance +
		       ", simulationDistance=" + simulationDistance +
		       ", reducedDebugInfo=" + reducedDebugInfo +
		       ", enableRespawnScreen=" + enableRespawnScreen +
		       ", doLimitedCrafting=" + doLimitedCrafting +
		       ", dimension=" + dimension +
		       ", worldName='" + worldName + '\'' +
		       ", hashedSeed=" + hashedSeed +
		       ", gameMode=" + gameMode +
		       ", previousGamemode=" + previousGamemode +
		       ", debug=" + debug +
		       ", flat=" + flat +
		       ", hasDeathLocation=" + hasDeathLocation +
		       ", lastDeathPos=" + lastDeathPos +
		       ", portalCooldown=" + portalCooldown +
		       ", enforcesSecureChat=" + enforcesSecureChat +
		       '}';
	}
}

