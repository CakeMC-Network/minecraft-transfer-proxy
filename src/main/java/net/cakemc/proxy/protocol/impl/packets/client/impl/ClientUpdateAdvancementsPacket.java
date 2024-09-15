package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;
import net.cakemc.mc.lib.game.Advancement;
import net.cakemc.mc.lib.game.Advancement.DisplayData;
import net.cakemc.mc.lib.game.inventory.item.ItemStack;
import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ClientUpdateAdvancementsPacket extends AbstractPacket {
	private static final int FLAG_HAS_BACKGROUND_TEXTURE = 0x01;
	private static final int FLAG_SHOW_TOAST = 0x02;
	private static final int FLAG_HIDDEN = 0x04;

	private boolean reset;
	private Advancement[] advancements;
	private String[] toRemove;
	private Map<String, Map<String, Long>> progress;

	public ClientUpdateAdvancementsPacket(boolean reset, Advancement[] advancements, String[] toRemove, Map<String, Map<String, Long>> progress) {
		this.reset = reset;
		this.advancements = advancements;
		this.toRemove = toRemove;
		this.progress = progress;
	}

	@Override
	public void read(ByteBuf buf) {
		this.reset = buf.readBoolean();

		this.advancements = new Advancement[ readVarInt(buf)];
		for (int i = 0; i < this.advancements.length; i++) {
			String id =  readString(buf);
			String parentId =  readNullable(buf, AbstractPacket::readString);
			DisplayData displayData =  readNullable(buf, subBuffer -> {
				BaseComponent title =  readComponent(subBuffer);
				BaseComponent description =  readComponent(subBuffer);
				ItemStack icon =  readOptionalItemStack(subBuffer);
				Advancement.Type advancementType = Advancement.Type.byIdx(readVarInt(subBuffer));

				int flags = subBuffer.readInt();
				boolean hasBackgroundTexture = (flags & FLAG_HAS_BACKGROUND_TEXTURE) != 0;
				boolean showToast = (flags & FLAG_SHOW_TOAST) != 0;
				boolean hidden = (flags & FLAG_HIDDEN) != 0;

				String backgroundTexture = hasBackgroundTexture ?  readString(subBuffer) : null;
				float posX = subBuffer.readFloat();
				float posY = subBuffer.readFloat();

				return new DisplayData(icon, title, description, advancementType, showToast, hidden, posX, posY, backgroundTexture);
			});

			List<List<String>> requirements =  readList(buf, subBuffer ->  readList(subBuffer, AbstractPacket::readString));

			boolean sendTelemetryEvent = buf.readBoolean();

			this.advancements[i] = new Advancement(id, requirements, parentId, displayData, sendTelemetryEvent);
		}

		this.toRemove = new String[ readVarInt(buf)];
		for (int i = 0; i < this.toRemove.length; i++) {
			this.toRemove[i] =  readString(buf);
		}

		this.progress = new HashMap<>();
		int progressCount =  readVarInt(buf);
		for (int i = 0; i < progressCount; i++) {
			String advancementId =  readString(buf);

			Map<String, Long> advancementProgress = new HashMap<>();
			int criterionCount =  readVarInt(buf);
			for (int j = 0; j < criterionCount; j++) {
				String criterionId =  readString(buf);
				long achievedDate = buf.readBoolean() ? buf.readLong() : -1;
				advancementProgress.put(criterionId, achievedDate);
			}

			this.progress.put(advancementId, advancementProgress);
		}
	}

	@Override
	public void write(ByteBuf buf) {
		buf.writeBoolean(this.reset);

		 writeVarInt(buf, this.advancements.length);
		for (Advancement advancement : this.advancements) {
			 writeString(buf, advancement.getId());
			if (advancement.getParentId() != null) {
				buf.writeBoolean(true);
				 writeString(buf, advancement.getParentId());
			} else {
				buf.writeBoolean(false);
			}

			 writeNullable(buf, advancement.getDisplayData(), (subBuffer, data) -> {
				 writeComponent(subBuffer, data.getTitle());
				 writeComponent(subBuffer, data.getDescription());
				 writeOptionalItemStack(subBuffer, data.getIcon());
				 writeVarInt(subBuffer, data.getAdvancementType().ordinal());

				int flags = 0;
				if (data.getBackgroundTexture() != null) {
					flags |= FLAG_HAS_BACKGROUND_TEXTURE;
				}

				if (data.isShowToast()) {
					flags |= FLAG_SHOW_TOAST;
				}

				if (data.isHidden()) {
					flags |= FLAG_HIDDEN;
				}

				 subBuffer.writeInt(flags);

				if (data.getBackgroundTexture() != null) {
					 writeString(subBuffer, data.getBackgroundTexture());
				}

				 subBuffer.writeFloat(data.getPosX());
				 subBuffer.writeFloat(data.getPosY());
			});

			 writeList(buf, advancement.getRequirements(), (subBuffer, requirement) ->  writeList(subBuffer, requirement, AbstractPacket::writeString));

			buf.writeBoolean(advancement.isSendsTelemetryEvent());
		}

		 writeVarInt(buf, this.toRemove.length);
		for (String id : this.toRemove) {
			 writeString(buf, id);
		}

		 writeVarInt(buf, this.progress.size());
		for (Entry<String, Map<String, Long>> advancement : this.progress.entrySet()) {
			 writeString(buf, advancement.getKey());
			Map<String, Long> advancementProgress = advancement.getValue();
			 writeVarInt(buf, advancementProgress.size());
			for (Entry<String, Long> criterion : advancementProgress.entrySet()) {
				 writeString(buf, criterion.getKey());
				if (criterion.getValue() != -1) {
					buf.writeBoolean(true);
					buf.writeLong(criterion.getValue());
				} else {
					buf.writeBoolean(false);
				}
			}
		}
	}

	public Advancement[] getAdvancements() {
		return advancements;
	}

	public Map<String, Map<String, Long>> getProgress() {
		return progress;
	}

	public String[] getToRemove() {
		return toRemove;
	}
}

