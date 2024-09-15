package net.cakemc.proxy.protocol.impl.packets.client.impl.spawn;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.EntityType;
import net.cakemc.mc.lib.game.entity.object.*;
import net.cakemc.mc.lib.network.AbstractPacket;
import net.cakemc.mc.lib.world.Direction;
import net.cakemc.mc.lib.world.Position;

import java.util.UUID;

public class ClientAddEntityPacket extends AbstractPacket {

	private int entityId;
	private UUID uuid;
	private EntityType type;
	private ObjectData data;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float headYaw;
	private float pitch;
	private double motionX;
	private double motionY;
	private double motionZ;

	public ClientAddEntityPacket(
		 int entityId, UUID uuid, EntityType type, ObjectData data,
		 Position position, double motionX, double motionY, double motionZ
	) {
		this(entityId, uuid, type, data,
		     position.getX(), position.getY(), position.getZ(),
		     position.getYaw(), position.getYaw(), position.getPitch(),
		     motionX, motionY, motionZ
		);
	}

	public ClientAddEntityPacket(
		 int entityId, UUID uuid, EntityType type, ObjectData data,
		 Position position

	) {
		this(entityId, uuid, type, data,
		     position.getX(), position.getY(), position.getZ(),
		     position.getYaw(), position.getYaw(), position.getPitch(),
		     0, 0, 0
		);
	}

	public ClientAddEntityPacket(
		 int entityId, UUID uuid, EntityType type, ObjectData data,
		 double x, double y, double z, float yaw, float headYaw,
		 float pitch, double motionX, double motionY, double motionZ
	) {

		this.entityId = entityId;
		this.uuid = uuid;
		this.type = type;
		this.data = data;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.headYaw = headYaw;
		this.pitch = pitch;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.uuid = readUUID(buf);
		this.type = EntityType.from(readVarInt(buf));
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.pitch = buf.readByte() * 360 / 256f;
		this.yaw = buf.readByte() * 360 / 256f;
		this.headYaw = buf.readByte() * 360 / 256f;

		int data = readVarInt(buf);
		if (this.type == EntityType.MINECART) {
			this.data = MinecartType.values()[data];
		} else if (this.type == EntityType.ITEM_FRAME || this.type == EntityType.GLOW_ITEM_FRAME || this.type == EntityType.PAINTING) {
			this.data = Direction.values()[data];
		} else if (this.type == EntityType.FALLING_BLOCK) {
			this.data = new FallingBlockData(data & 65535, data >> 16);
		} else if (this.type.isProjectile()) {
			this.data = new ProjectileData(data);
		} else if (this.type == EntityType.WARDEN) {
			this.data = new WardenData(data);
		} else {
			if (data == 0) {
				this.data = new GenericObjectData(0);
			} else {
				this.data = new GenericObjectData(data);
			}
		}

		this.motionX = buf.readShort() / 8000D;
		this.motionY = buf.readShort() / 8000D;
		this.motionZ = buf.readShort() / 8000D;
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		writeUUID(buf, this.uuid);
		writeVarInt(buf, this.type.ordinal());
		buf.writeDouble(this.x);
		buf.writeDouble(this.y);
		buf.writeDouble(this.z);
		buf.writeByte((byte) (this.pitch * 256 / 360));
		buf.writeByte((byte) (this.yaw * 256 / 360));
		buf.writeByte((byte) (this.headYaw * 256 / 360));

		int data = 0;
		//if (this.data instanceof MinecartType) {
		//	data = ((MinecartType) this.data).ordinal();
		//} else if (this.data instanceof Direction) {
		//	data = ((Direction) this.data).ordinal();
		//} else if (this.data instanceof FallingBlockData) {                   todo write
		//	data = ((FallingBlockData) this.data).getId() | ((FallingBlockData) this.data).getMetadata() << 16;
		//} else if (this.data instanceof ProjectileData) {
		//	data = ((ProjectileData) this.data).getOwnerId();
		//} else if (this.data instanceof GenericObjectData) {
		//	data = ((GenericObjectData) this.data).getValue();
		//}

		writeVarInt(buf, data);

		buf.writeShort((int) (this.motionX * 8000));
		buf.writeShort((int) (this.motionY * 8000));
		buf.writeShort((int) (this.motionZ * 8000));
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public ObjectData getData() {
		return data;
	}

	public void setData(ObjectData data) {
		this.data = data;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getHeadYaw() {
		return headYaw;
	}

	public void setHeadYaw(float headYaw) {
		this.headYaw = headYaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public double getMotionX() {
		return motionX;
	}

	public void setMotionX(double motionX) {
		this.motionX = motionX;
	}

	public double getMotionY() {
		return motionY;
	}

	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}

	public double getMotionZ() {
		return motionZ;
	}

	public void setMotionZ(double motionZ) {
		this.motionZ = motionZ;
	}
}

