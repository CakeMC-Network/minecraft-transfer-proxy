package net.cakemc.proxy.protocol.impl.packets.client.impl.inventory;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.inventory.VillagerTrade;
import net.cakemc.mc.lib.game.inventory.item.ItemStack;
import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientMerchantOffersPacket extends AbstractPacket {

	private int containerId;
	private VillagerTrade[] trades;
	private int villagerLevel;
	private int experience;
	private boolean regularVillager;
	private boolean canRestock;

	public ClientMerchantOffersPacket(int containerId, VillagerTrade[] trades, int villagerLevel, int experience, boolean regularVillager, boolean canRestock) {
		this.containerId = containerId;
		this.trades = trades;
		this.villagerLevel = villagerLevel;
		this.experience = experience;
		this.regularVillager = regularVillager;
		this.canRestock = canRestock;
	}

	public ClientMerchantOffersPacket() {
	}

	@Override
	public void read(ByteBuf buf) {
		this.containerId = readVarInt(buf);

		int size = readVarInt(buf);
		this.trades = new VillagerTrade[size];
		for (int i = 0 ; i < trades.length ; i++) {
			ItemStack firstInput = readTradeItemStack(buf);
			ItemStack output = readOptionalItemStack(buf);
			ItemStack secondInput = readNullable(buf, AbstractPacket::readTradeItemStack);

			boolean tradeDisabled = buf.readBoolean();
			int numUses = buf.readInt();
			int maxUses = buf.readInt();
			int xp = buf.readInt();
			int specialPrice = buf.readInt();
			float priceMultiplier = buf.readFloat();
			int demand = buf.readInt();

			this.trades[i] = new VillagerTrade(firstInput, secondInput, output, tradeDisabled, numUses, maxUses, xp, specialPrice, priceMultiplier, demand);
		}

		this.villagerLevel = readVarInt(buf);
		this.experience = readVarInt(buf);
		this.regularVillager = buf.readBoolean();
		this.canRestock = buf.readBoolean();
	}

	@Override
	public void write(ByteBuf buf) {
		writeVarInt(buf, this.containerId);

		writeVarInt(buf, this.trades.length);
		for (VillagerTrade trade : this.trades) {
			writeTradeItemStack(buf, trade.getFirstInput());
			writeOptionalItemStack(buf, trade.getOutput());
			writeNullable(buf, trade.getSecondInput(), AbstractPacket::writeTradeItemStack);

			buf.writeBoolean(trade.isTradeDisabled());
			buf.writeInt(trade.getNumUses());
			buf.writeInt(trade.getMaxUses());
			buf.writeInt(trade.getXp());
			buf.writeInt(trade.getSpecialPrice());
			buf.writeFloat(trade.getPriceMultiplier());
			buf.writeInt(trade.getDemand());
		}

		writeVarInt(buf, this.villagerLevel);
		writeVarInt(buf, this.experience);
		buf.writeBoolean(this.regularVillager);
		buf.writeBoolean(this.canRestock);
	}

	public int getContainerId() {
		return containerId;
	}

	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}

	public VillagerTrade[] getTrades() {
		return trades;
	}

	public void setTrades(VillagerTrade[] trades) {
		this.trades = trades;
	}

	public int getVillagerLevel() {
		return villagerLevel;
	}

	public void setVillagerLevel(int villagerLevel) {
		this.villagerLevel = villagerLevel;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public boolean isRegularVillager() {
		return regularVillager;
	}

	public void setRegularVillager(boolean regularVillager) {
		this.regularVillager = regularVillager;
	}

	public boolean isCanRestock() {
		return canRestock;
	}

	public void setCanRestock(boolean canRestock) {
		this.canRestock = canRestock;
	}
}

