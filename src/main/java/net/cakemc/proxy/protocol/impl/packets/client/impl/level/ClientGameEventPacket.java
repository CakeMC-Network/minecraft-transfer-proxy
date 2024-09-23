package net.cakemc.proxy.protocol.impl.packets.client.impl.level;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.player.GameMode;
import net.cakemc.mc.lib.game.entity.player.notify.Value;
import net.cakemc.mc.lib.game.entity.player.notify.Value.Type;
import net.cakemc.mc.lib.game.entity.player.notify.impl.*;
import net.cakemc.mc.lib.game.entity.player.notify.impl.EnterCredits.Seen;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientGameEventPacket extends AbstractPacket {

	private Type type;
	private Value clientValue;

	public ClientGameEventPacket() {
	}

	public ClientGameEventPacket(Value value) {
		this.type = value.getType();
		this.clientValue = value;
	}

	@Override
	public void read(ByteBuf buf) {
		this.type = Type.values()[buf.readUnsignedByte()];

		float value = buf.readFloat();

		clientValue = switch (type) {
			case CHANGE_GAMEMODE -> GameMode.values()[(int) value];
			case ENTER_CREDITS -> new EnterCredits(Seen.values()[(int) value]);
			case DEMO_MESSAGE -> new DemoMessage(DemoMessage.Current.values()[(int) value]);
			case ENABLE_RESPAWN_SCREEN -> new RespawnScreen(RespawnScreen.Current.values()[(int) value]);
			case RAIN_STRENGTH -> new RainStrength(value);
			case THUNDER_STRENGTH -> new ThunderStrength(value);
			case AFFECTED_BY_ELDER_GUARDIAN -> new ElderGuardianEffect(value);
			case LIMITED_CRAFTING -> new LimitedCrafting(LimitedCrafting.Limitation.values()[(int) value]);

			// todo implement the rest of the events
			default -> this.clientValue = null;
		};
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeByte(this.type.ordinal());
		float value = switch (type) {
			case CHANGE_GAMEMODE -> ((GameMode) clientValue).ordinal();
			case DEMO_MESSAGE -> ((DemoMessage) clientValue).getCurrent().ordinal();
			case ENTER_CREDITS -> ((EnterCredits) clientValue).getSeen().ordinal();
			case ENABLE_RESPAWN_SCREEN -> ((RespawnScreen) clientValue).getCurrent().ordinal();
			case RAIN_STRENGTH -> ((RainStrength) clientValue).getStrength();
			case THUNDER_STRENGTH -> ((ThunderStrength) clientValue).getStrength();
			case AFFECTED_BY_ELDER_GUARDIAN -> clientValue.getType().ordinal();
			case LIMITED_CRAFTING -> ((LimitedCrafting) clientValue).getLimitation().ordinal();
			case LEVEL_CHUNKS_LOAD_START -> ((StartWaitingForChunk) clientValue).getValue();

			default -> 0;
		};

		buf.writeFloat(value);
	}

	public Type getType() {
		return type;
	}

	public Value getClientValue() {
		return clientValue;
	}
}

