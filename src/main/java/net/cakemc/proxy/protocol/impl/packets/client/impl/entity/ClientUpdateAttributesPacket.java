package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.attribute.Attribute;
	 import net.cakemc.mc.lib.game.entity.attribute.AttributeModifier;
	 import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.List;

public class ClientUpdateAttributesPacket extends AbstractPacket {

	private final int entityId;
	private final List<Attribute> attributes;

	public ClientUpdateAttributesPacket(int entityId, List<Attribute> attributes) {
		this.entityId = entityId;
		this.attributes = attributes;
	}

	@Override
	public void read(ByteBuf buf) {

	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);

		writeVarInt(buf, attributes.size());
		for (Attribute attribute : attributes) {
			writeVarInt(buf, attribute.getType().getId());
			buf.writeDouble(attribute.getValue());
			writeVarInt(buf, attribute.getModifiers().size());
			for (AttributeModifier modifier : attribute.getModifiers()) {
				writeString(buf, modifier.getId());
				buf.writeDouble(modifier.getAmount());
				writeVarInt(buf, modifier.getOperation().ordinal());
			}
		}
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public int getEntityId() {
		return entityId;
	}
}

