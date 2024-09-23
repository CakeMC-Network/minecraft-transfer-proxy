package net.cakemc.proxy.protocol.impl.packets.client.impl.entity;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.Pose;
import net.cakemc.mc.lib.game.entity.metadata.EntityMetaDataChanges;
import net.cakemc.mc.lib.game.entity.metadata.MetaDataEntry;
import net.cakemc.mc.lib.game.inventory.item.ItemStack;
import net.cakemc.mc.lib.game.nbt.NBTType;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Direction;
import net.cakemc.mc.lib.world.WorldPosition;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class ClientSetEntityDataPacket extends AbstractPacket {

	private int entityId;
	private EntityMetaDataChanges changes;

	public ClientSetEntityDataPacket(int entityId, EntityMetaDataChanges changes) {
		this.entityId = entityId;
		this.changes = changes;
	}

	public ClientSetEntityDataPacket(int entityId, Map<Integer, MetaDataEntry<?>> changes) {
		this.entityId = entityId;

		this.changes = new EntityMetaDataChanges(entityId, changes);
	}

	public ClientSetEntityDataPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		// todo
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, entityId);

		for (Entry<Integer, MetaDataEntry<?>> entry : changes.getMetadata().entrySet()) {

			buf.writeByte(entry.getKey());
			writeVarInt(buf, entry.getValue().getType().ordinal());

			switch (entry.getValue().getType()) {
				case BYTE_TYPE -> buf.writeByte((byte) entry.getValue().getValue());
				case VAR_INT_TYPE -> writeVarInt(buf, (int) entry.getValue().getValue());
				case VAR_LONG_TYPE -> writeVarLong(buf, (long) entry.getValue().getValue());
				case FLOAT_TYPE -> buf.writeFloat((float) entry.getValue().getValue());
				case STRING_TYPE -> writeString(buf, (String) entry.getValue().getValue());
				case NBT_TYPE -> writeNBT(buf, (NBTType<?>) entry.getValue().getValue());
				case CHAT_TYPE -> writeComponent(buf, (BaseComponent) entry.getValue().getValue());
				case OPT_CHAT_TYPE -> writeNullable(buf, (BaseComponent) entry.getValue().getValue(), AbstractPacket::writeComponent);
				case SLOT_TYPE -> writeItemStack(buf, (ItemStack) entry.getValue().getValue());
				case BOOLEAN_TYPE -> buf.writeBoolean((boolean) entry.getValue().getValue());
				case POSITION_TYPE -> writePosition(buf, (WorldPosition) entry.getValue().getValue());
				case POSE_TYPE -> writeVarInt(buf, ((Pose) entry.getValue().getValue()).ordinal());
				case OPT_POSITION_TYPE -> writeNullable(buf, (WorldPosition) entry.getValue().getValue(), AbstractPacket::writePosition);
				case OPT_UUID_TYPE -> writeNullable(buf, (UUID) entry.getValue().getValue(), AbstractPacket::writeUUID);
				case DIRECTION_TYPE -> writeVarInt(buf, ((Direction) entry.getValue().getValue()).ordinal());
			}
		}

		buf.writeByte(255);
	}


	public int getEntityId() {
		return entityId;
	}

	public EntityMetaDataChanges getChanges() {
		return changes;
	}
}

