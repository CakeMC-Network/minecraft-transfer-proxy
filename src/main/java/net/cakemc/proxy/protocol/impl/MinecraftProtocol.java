package net.cakemc.proxy.protocol.impl;

import net.cakemc.mc.lib.game.nbt.NBTComponent;
import net.cakemc.mc.lib.game.nbt.NBTInputStream;
import net.cakemc.mc.lib.game.nbt.NbtUtils;
import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol;
import net.cakemc.proxy.protocol.impl.packets.client.ProtocolClientPackets;
import net.cakemc.proxy.protocol.impl.packets.server.ProtocolServerPackets;

import java.io.IOException;
import java.io.InputStream;

public final class MinecraftProtocol extends AbstractProtocol {
	public static final int DEFAULT_SERVER_THRESHOLD = 256;
	private final ProtocolClientPackets inboundPackets;
	private final ProtocolServerPackets outboundPackets;
	public NBTComponent clientDefaultRegistry;

	public MinecraftProtocol() {
		this.inboundPackets = new ProtocolClientPackets();
		this.outboundPackets = new ProtocolServerPackets();
	}

	@Override
	public void register() {
		try {
			InputStream inputStream = MinecraftProtocol.class.getResourceAsStream("/client_default_registries.nbt");
			NBTInputStream nbtInputStream = NbtUtils.createGZIPReader(inputStream);
			clientDefaultRegistry = (NBTComponent) nbtInputStream.readTag(512);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		inboundPackets.register();
		outboundPackets.register();
	}

	public ProtocolClientPackets getInbounds() {
		return this.inboundPackets;
	}

	public ProtocolServerPackets getOutbounds() {
		return this.outboundPackets;
	}

	public NBTComponent getClientDefaultRegistry() {
		return clientDefaultRegistry;
	}
}
