package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Vector;

public class ClientDamageEventPacket extends AbstractPacket {

	private int entityId;
	private int sourceTypeId;
	private int sourceCauseId;
	private int sourceDirectId;
	private Vector sourcePosition;

	public ClientDamageEventPacket(int entityId, int sourceTypeId, int sourceCauseId, int sourceDirectId, Vector sourcePosition) {
		this.entityId = entityId;
		this.sourceTypeId = sourceTypeId;
		this.sourceCauseId = sourceCauseId;
		this.sourceDirectId = sourceDirectId;
		this.sourcePosition = sourcePosition;
	}

	public ClientDamageEventPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.sourceTypeId = readVarInt(buf);
		this.sourceCauseId = readVarInt(buf) - 1;
		this.sourceDirectId = readVarInt(buf) - 1;
		this.sourcePosition = buf.readBoolean() ? new Vector(buf.readDouble(), buf.readDouble(), buf.readDouble()) : null;
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		writeVarInt(buf, this.sourceTypeId);
		writeVarInt(buf, this.sourceCauseId + 1);
		writeVarInt(buf, this.sourceDirectId + 1);

		if (this.sourcePosition != null) {
			buf.writeBoolean(true);
			buf.writeDouble(this.sourcePosition.getX());
			buf.writeDouble(this.sourcePosition.getY());
			buf.writeDouble(this.sourcePosition.getZ());
		} else {
			buf.writeBoolean(false);
		}
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public int getSourceTypeId() {
		return sourceTypeId;
	}

	public void setSourceTypeId(int sourceTypeId) {
		this.sourceTypeId = sourceTypeId;
	}

	public int getSourceCauseId() {
		return sourceCauseId;
	}

	public void setSourceCauseId(int sourceCauseId) {
		this.sourceCauseId = sourceCauseId;
	}

	public int getSourceDirectId() {
		return sourceDirectId;
	}

	public void setSourceDirectId(int sourceDirectId) {
		this.sourceDirectId = sourceDirectId;
	}

	public Vector getSourcePosition() {
		return sourcePosition;
	}

	public void setSourcePosition(Vector sourcePosition) {
		this.sourcePosition = sourcePosition;
	}
}

