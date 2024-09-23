package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.PlayerSpawnInfo;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientRespawnPacket extends AbstractPacket {

	private static final byte KEEP_ATTRIBUTE_MODIFIERS = 1;
	private static final byte KEEP_ENTITY_DATA = 2;

	private PlayerSpawnInfo commonPlayerSpawnInfo;

	private boolean keepMetadata;
	private boolean keepAttributeModifiers;

	public ClientRespawnPacket(PlayerSpawnInfo commonPlayerSpawnInfo, boolean keepMetadata, boolean keepAttributeModifiers) {
		this.commonPlayerSpawnInfo = commonPlayerSpawnInfo;
		this.keepMetadata = keepMetadata;
		this.keepAttributeModifiers = keepAttributeModifiers;
	}

	public ClientRespawnPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.commonPlayerSpawnInfo = readPlayerSpawnInfo(buf);
		byte dataToKeep = buf.readByte();
		this.keepAttributeModifiers = (dataToKeep & KEEP_ATTRIBUTE_MODIFIERS) != 0;
		this.keepMetadata = (dataToKeep & KEEP_ENTITY_DATA) != 0;
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, commonPlayerSpawnInfo.dimension());
		writeString(buf, commonPlayerSpawnInfo.worldName());
		buf.writeLong(commonPlayerSpawnInfo.hashedSeed());
		buf.writeByte(commonPlayerSpawnInfo.gameMode().ordinal());
		buf.writeByte(-1);
		buf.writeBoolean(false);
		buf.writeBoolean(false);
		buf.writeBoolean(false);
		writeVarInt(buf, 0);

		byte dataToKeep = 0;
		if (this.keepMetadata) {
			dataToKeep += KEEP_ENTITY_DATA;
		}
		if (this.keepAttributeModifiers) {
			dataToKeep += KEEP_ATTRIBUTE_MODIFIERS;
		}
		buf.writeByte(dataToKeep);
	}

}

