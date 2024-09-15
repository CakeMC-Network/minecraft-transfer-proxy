package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.nbt.NBTComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

public class ClientRegistryDataPacket extends AbstractPacket {
	private String registry;
	private List<RegistryEntry> entries;

	public ClientRegistryDataPacket(String registry, List<RegistryEntry> entries) {
		this.registry = registry;
		this.entries = entries;
	}

	public ClientRegistryDataPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.registry = readString(buf);

		int entries = readVarInt(buf);
		this.entries = new ArrayList<>();
		for (int i = 0 ; i < entries ; i++) {
			String name = readString(buf);
			boolean present = buf.readBoolean();
			NBTComponent nbtComponent = null;
			if (present) {
				nbtComponent = (NBTComponent) readNBT(buf);
			}

			this.entries.add(new RegistryEntry(name, nbtComponent));
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeString(buf, this.registry);

		writeVarInt(buf, entries.size());
		for (RegistryEntry entry : entries) {
			writeString(buf, entry.getId());

			boolean present = entry.getData() != null;
			buf.writeBoolean(present);
			if (present) {
				writeNBT(buf, entry.getData());
			}
		}
	}

	public String getRegistry() {
		return registry;
	}

	public void setRegistry(String registry) {
		this.registry = registry;
	}

	public List<RegistryEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<RegistryEntry> entries) {
		this.entries = entries;
	}

	public static final class RegistryEntry {
		private final String id;
		private final NBTComponent data;

		public RegistryEntry(String id, NBTComponent data) {
			this.id = id;
			this.data = data;
		}

		public NBTComponent getData() {
			return data;
		}

		public String getId() {
			return id;
		}
	}
}

