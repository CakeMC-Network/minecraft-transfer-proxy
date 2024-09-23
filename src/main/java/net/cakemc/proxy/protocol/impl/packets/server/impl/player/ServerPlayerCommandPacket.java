package net.cakemc.proxy.protocol.impl.packets.server.impl.player;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.game.entity.player.PlayerState;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ServerPlayerCommandPacket extends AbstractPacket {
	private int entityId;
	private PlayerState state;
	private int jumpBoost;

	public ServerPlayerCommandPacket(int entityId, PlayerState state, int jumpBoost) {
		this.entityId = entityId;
		this.state = state;
		this.jumpBoost = jumpBoost;
	}

	public ServerPlayerCommandPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.entityId = readVarInt(buf);
		this.state = PlayerState.values()[readVarInt(buf)];
		this.jumpBoost = readVarInt(buf);
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.entityId);
		writeVarInt(buf, this.state.ordinal());
		writeVarInt(buf, this.jumpBoost);
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public PlayerState getState() {
		return state;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public int getJumpBoost() {
		return jumpBoost;
	}

	public void setJumpBoost(int jumpBoost) {
		this.jumpBoost = jumpBoost;
	}

	@Override public String toString() {
		return "ServerPlayerCommandPacket{" +
		       "entityId=" + entityId +
		       ", state=" + state +
		       ", jumpBoost=" + jumpBoost +
		       '}';
	}
}

