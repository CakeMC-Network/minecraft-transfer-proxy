package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.entity.EntityType;
import net.cakemc.mc.lib.game.entity.player.Statistic;
import net.cakemc.mc.lib.game.statistic.*;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.Map;

public class ClientAwardStatsPacket extends AbstractPacket {

	private Map<Statistic, Integer> statisticMap;

	public ClientAwardStatsPacket(Map<Statistic, Integer> statisticMap) {
		this.statisticMap = statisticMap;
	}

	public ClientAwardStatsPacket() {

	}

	@Override
	public void read(ByteBuf buf) {
		int length = readVarInt(buf);
		for (int index = 0; index < length; index++) {
			StatisticCategory category = StatisticCategory.values()[readVarInt(buf)];
			int statisticId = readVarInt(buf);
			Statistic statistic = switch (category) {
				case BREAK_BLOCK -> new BreakBlockStatistic(statisticId);
				case CRAFT_ITEM -> new CraftItemStatistic(statisticId);
				case USE_ITEM -> new UseItemStatistic(statisticId);
				case BREAK_ITEM -> new BreakItemStatistic(statisticId);
				case PICKED_UP_ITEM -> new PickupItemStatistic(statisticId);
				case DROP_ITEM -> new DropItemStatistic(statisticId);
				case KILL_ENTITY -> new KillEntityStatistic(EntityType.from(statisticId));
				case KILLED_BY_ENTITY -> new KilledByEntityStatistic(EntityType.from(statisticId));
				case CUSTOM -> CustomStatistic.byId(statisticId);
			};
			this.statisticMap.put(statistic, readVarInt(buf));
		}
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.statisticMap.size());
		for (Map.Entry<Statistic, Integer> entry : statisticMap.entrySet()) {
			Statistic statistic = entry.getKey();

			StatisticCategory category;
			int statisticId;
			switch (statistic) {
				case BreakBlockStatistic breakBlockStatistic -> {
					category = StatisticCategory.BREAK_BLOCK;
					statisticId = breakBlockStatistic.getId();
				}
				case CraftItemStatistic craftItemStatistic -> {
					category = StatisticCategory.CRAFT_ITEM;
					statisticId = craftItemStatistic.getId();
				}
				case UseItemStatistic useItemStatistic -> {
					category = StatisticCategory.USE_ITEM;
					statisticId = useItemStatistic.getId();
				}
				case BreakItemStatistic breakItemStatistic -> {
					category = StatisticCategory.BREAK_ITEM;
					statisticId = breakItemStatistic.getId();
				}
				case PickupItemStatistic pickupItemStatistic -> {
					category = StatisticCategory.PICKED_UP_ITEM;
					statisticId = pickupItemStatistic.getId();
				}
				case DropItemStatistic dropItemStatistic -> {
					category = StatisticCategory.DROP_ITEM;
					statisticId = dropItemStatistic.getId();
				}
				case KillEntityStatistic killEntityStatistic -> {
					category = StatisticCategory.KILL_ENTITY;
					statisticId = killEntityStatistic.getEntity().ordinal();
				}
				case KilledByEntityStatistic killedByEntityStatistic -> {
					category = StatisticCategory.KILLED_BY_ENTITY;
					statisticId = killedByEntityStatistic.getEntity().ordinal();
				}
				case CustomStatistic customStatistic -> {
					category = StatisticCategory.CUSTOM;
					statisticId = customStatistic.ordinal();
				}
				case null, default -> throw new IllegalStateException();
			}
			writeVarInt(buf, category.ordinal());
			writeVarInt(buf, statisticId);
			writeVarInt(buf, entry.getValue());
		}
	}

}

