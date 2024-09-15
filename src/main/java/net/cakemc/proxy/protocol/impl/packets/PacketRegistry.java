package net.cakemc.proxy.protocol.impl.packets;

import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.api.packets.AbstractPacketRegistry;
import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol.State;
import org.jetbrains.annotations.Nullable;
import speiger.src.collections.objects.maps.impl.misc.Object2ObjectArrayMap;
import speiger.src.collections.objects.misc.pairs.impl.ObjectIntImmutablePair;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public final class PacketRegistry extends AbstractPacketRegistry {
	private final Object2ObjectArrayMap<ObjectIntImmutablePair<State>,
		 Class<? extends AbstractPacket>> packets = new Object2ObjectArrayMap<>();

	private int idHandshake = 0x0;
	private int idLogin = 0x0;
	private int idGame = 0x0;
	private int idStatus = 0x0;
	private int idConfig = 0x0;

	@Override
	public <P extends AbstractPacket> void registerById(
		 final State state,
		 final int id,
		 final Class<P> clazz
	) {
		this.packets.put(
			 new ObjectIntImmutablePair<>(state, id), clazz);
	}

	@Override public <P extends AbstractPacket> void registerAutoId(State state, Class<P> clazz) {
		this.registerById(state, switch (state) {
			case HANDSHAKE -> idHandshake++;
			case LOGIN -> idLogin++;
			case GAME -> idGame++;
			case STATUS -> idStatus++;
			case CONFIG -> idConfig++;
		}, clazz);
	}

	@Override
	public @Nullable <P extends AbstractPacket> P construct(State state, int id) {
		try {
			final Class<? extends AbstractPacket> clazz = this.packets.get(new ObjectIntImmutablePair<>(state, id));

			return clazz == null ? null : (P) clazz.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public @Nullable <P extends AbstractPacket> Class<P> byId(State state, int id) {
		final Class<? extends AbstractPacket> clazz = this.packets.get(new ObjectIntImmutablePair<>(state, id));

		return (Class<P>) clazz;
	}

	@Override
	public <P extends AbstractPacket> int byPacketClass(State state, Class<P> clazz) {
		return this.getPackets().entrySet().stream()
		           .filter(entry -> entry.getValue().equals(clazz) && entry.getKey().getKey() == state)
		           .findFirst().map(entry -> entry.getKey().getIntValue()).orElse(-1);
	}

	public Object2ObjectArrayMap<ObjectIntImmutablePair<State>, Class<? extends AbstractPacket>> getPackets() {
		return packets;
	}
}
