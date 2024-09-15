package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.AbstractPlayer;
import net.cakemc.mc.lib.game.entity.player.AbstractPlayer.Hand;
import net.cakemc.mc.lib.game.entity.player.AbstractPlayer.Hand.Type;
import net.cakemc.mc.lib.game.entity.player.AbstractPlayer.InteractAction;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerInteractPacket extends AbstractPacket {

	private int entityId;
	private InteractAction action;

	private float targetX;
	private float targetY;
	private float targetZ;
	private Type hand;
	private boolean isSneaking;

	public ServerInteractPacket(int entityId, InteractAction action, float targetX, float targetY, float targetZ, Type hand, boolean isSneaking) {
		this.entityId = entityId;
		this.action = action;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
		this.hand = hand;
		this.isSneaking = isSneaking;
	}

	public ServerInteractPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.action = InteractAction.values()[readVarInt(buf)];
		if (this.action == InteractAction.INTERACT_AT) {
			this.targetX = buf.readFloat();
			this.targetY = buf.readFloat();
			this.targetZ = buf.readFloat();
		} else {
			this.targetX = 0;
			this.targetY = 0;
			this.targetZ = 0;
		}

		if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
			this.hand = Type.values()[readVarInt(buf)];
		} else {
			this.hand = null;
		}
		this.isSneaking = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		writeVarInt(buf, this.action.ordinal());
		if (this.action == InteractAction.INTERACT_AT) {
			buf.writeFloat(this.targetX);
			buf.writeFloat(this.targetY);
			buf.writeFloat(this.targetZ);
		}

		if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
			writeVarInt(buf, this.hand.ordinal());
		}
		buf.writeBoolean(this.isSneaking);
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public InteractAction getAction() {
		return action;
	}

	public void setAction(InteractAction action) {
		this.action = action;
	}

	public float getTargetX() {
		return targetX;
	}

	public void setTargetX(float targetX) {
		this.targetX = targetX;
	}

	public float getTargetY() {
		return targetY;
	}

	public void setTargetY(float targetY) {
		this.targetY = targetY;
	}

	public float getTargetZ() {
		return targetZ;
	}

	public void setTargetZ(float targetZ) {
		this.targetZ = targetZ;
	}

	public Type getHand() {
		return hand;
	}

	public void setHand(Type hand) {
		this.hand = hand;
	}

	public boolean isSneaking() {
		return isSneaking;
	}

	public void setSneaking(boolean sneaking) {
		isSneaking = sneaking;
	}

	@Override public String toString() {
		return "ServerInteractPacket{" +
		       "entityId=" + entityId +
		       ", action=" + action +
		       ", targetX=" + targetX +
		       ", targetY=" + targetY +
		       ", targetZ=" + targetZ +
		       ", hand=" + hand +
		       ", isSneaking=" + isSneaking +
		       '}';
	}
}

