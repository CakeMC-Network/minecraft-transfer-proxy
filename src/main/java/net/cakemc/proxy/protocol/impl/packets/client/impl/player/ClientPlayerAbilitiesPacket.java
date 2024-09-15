package net.cakemc.proxy.protocol.impl.packets.client.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientPlayerAbilitiesPacket extends AbstractPacket {

	private static final int FLAG_INVINCIBLE = 0x01;
	private static final int FLAG_FLYING = 0x02;
	private static final int FLAG_CAN_FLY = 0x04;
	private static final int FLAG_CREATIVE = 0x08;

	private boolean invincible;
	private boolean canFly;
	private boolean flying;
	private boolean creative;
	private float flySpeed;
	private float fieldOfView;

	public ClientPlayerAbilitiesPacket(
		 boolean invincible,
		 boolean canFly, boolean flying,
		 boolean creative, float flySpeed,
		 float fieldOfView
	) {
		this.invincible = invincible;
		this.canFly = canFly;
		this.flying = flying;
		this.creative = creative;
		this.flySpeed = flySpeed;
		this.fieldOfView = fieldOfView;
	}

	public ClientPlayerAbilitiesPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		byte flags = buf.readByte();
		this.invincible = (flags & FLAG_INVINCIBLE) > 0;
		this.canFly = (flags & FLAG_CAN_FLY) > 0;
		this.flying = (flags & FLAG_FLYING) > 0;
		this.creative = (flags & FLAG_CREATIVE) > 0;

		this.flySpeed = buf.readFloat();
		this.fieldOfView = buf.readFloat();
	}

	@Override
	public void write(ByteBuf buf) {
		int flags = 0;
		if (this.invincible) {
			flags |= FLAG_INVINCIBLE;
		}

		if (this.canFly) {
			flags |= FLAG_CAN_FLY;
		}

		if (this.flying) {
			flags |= FLAG_FLYING;
		}

		if (this.creative) {
			flags |= FLAG_CREATIVE;
		}

		buf.writeByte(flags);

		buf.writeFloat(this.flySpeed);
		buf.writeFloat(this.fieldOfView);
	}

	public boolean isInvincible() {
		return invincible;
	}

	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}

	public boolean isCanFly() {
		return canFly;
	}

	public void setCanFly(boolean canFly) {
		this.canFly = canFly;
	}

	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	public boolean isCreative() {
		return creative;
	}

	public void setCreative(boolean creative) {
		this.creative = creative;
	}

	public float getFlySpeed() {
		return flySpeed;
	}

	public void setFlySpeed(float flySpeed) {
		this.flySpeed = flySpeed;
	}

	public float getFieldOfView() {
		return fieldOfView;
	}

	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView;
	}
}

