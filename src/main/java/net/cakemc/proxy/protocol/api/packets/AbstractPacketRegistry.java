package net.cakemc.proxy.protocol.api.packets;

import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPacketRegistry {
	public abstract <P extends AbstractPacket> void registerById(AbstractProtocol.State state, final int id, Class<P> clazz);

	public abstract <P extends AbstractPacket> void registerAutoId(AbstractProtocol.State state, Class<P> clazz);

	public abstract @Nullable <P extends AbstractPacket> P construct(AbstractProtocol.State state, int id) throws NoSuchMethodException,
	                                                                                                              InvocationTargetException,
	                                                                                                              InstantiationException,
	                                                                                                              IllegalAccessException;

	public abstract @Nullable <P extends AbstractPacket> Class<P> byId(AbstractProtocol.State state, int id);

	public abstract <P extends AbstractPacket> int byPacketClass(AbstractProtocol.State state, Class<P> packetClazz);
}
