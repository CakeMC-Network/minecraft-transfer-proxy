package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.List;
import java.util.UUID;

public class ClientPlayerInfoRemovePacket extends AbstractPacket {

	private final List<UUID> profileIds;

	public ClientPlayerInfoRemovePacket(List<UUID> profileIds) {
		this.profileIds = profileIds;
	}

	@Override
	public void read(ByteBuf buf) {
		int size = readVarInt(buf);
		for (int i = 0 ; i < size ; i++) {
			profileIds.add(readUUID(buf));
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.profileIds.size());
		for (UUID profileId : profileIds) {
			writeUUID(buf, profileId);
		}
	}

	public List<UUID> getProfileIds() {
		return profileIds;
	}
}

