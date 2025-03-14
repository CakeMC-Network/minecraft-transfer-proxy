package net.cakemc.proxy.protocol.impl.packets.server;

import net.cakemc.proxy.protocol.api.protocol.AbstractProtocol;
import net.cakemc.proxy.protocol.impl.packets.PacketRegistry;
import net.cakemc.proxy.protocol.impl.packets.client.impl.ServerCookieResponsePacket;
import net.cakemc.proxy.protocol.impl.packets.server.impl.*;
import net.cakemc.proxy.protocol.impl.packets.server.impl.inventory.*;
import net.cakemc.proxy.protocol.impl.packets.server.impl.level.*;
import net.cakemc.proxy.protocol.impl.packets.server.impl.player.*;

public final class ProtocolServerPackets extends AbstractProtocol {
	private final PacketRegistry packetRegistry = new PacketRegistry();

	@Override
	public void register() {
		// status
		this.packetRegistry.registerAutoId(
			 State.STATUS,
			 ServerStatusRequestPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.STATUS,
			 ServerPingRequestPacket.class
		);

		// handshake
		this.packetRegistry.registerAutoId(
			 State.HANDSHAKE,
			 ClientIntentionPacket.class
		);

		// login
		this.packetRegistry.registerAutoId(
			 State.LOGIN,
			 ServerHelloPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.LOGIN,
			 ServerKeyPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.LOGIN,
			 ServerCustomQueryAnswerPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.LOGIN,
			 ServerLoginAcknowledgedPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.LOGIN,
			 ServerCookieResponsePacket.class
		);

		// config
		this.packetRegistry.registerAutoId(
			 State.CONFIG,
			 ServerClientInformationPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.CONFIG,
			 ServerCookieResponsePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.CONFIG,
			 ServerCustomPayloadPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.CONFIG,
			 ServerFinishConfigurationPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.CONFIG,
			 ServerKeepAlivePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.CONFIG,
			 ServerPongPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.CONFIG,
			 ServerResourcePackPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.CONFIG,
			 ServerSelectKnownPacks.class
		);

		// in-game
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerAcceptTeleportationPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerBlockEntityTagQueryPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerChangeDifficultyPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerChatAckPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerChatCommandPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerChatCommandSignedPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerChatPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerChatSessionUpdatePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerChunkBatchReceivedPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerClientCommandPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerClientInformationPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerCommandSuggestionPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerConfigurationAcknowledgedPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerContainerButtonClickPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerContainerClickPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerContainerClosePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerContainerSlotStateChangedPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerCookieResponsePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerCustomPayloadPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerDebugSampleSubscriptionPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerEditBookPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerEntityTagQuery.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerInteractPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerJigsawGeneratePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerKeepAlivePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerLockDifficultyPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerMovePlayerPosPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerMovePlayerPosRotPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerMovePlayerRotPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerMovePlayerStatusOnlyPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerMoveVehiclePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPaddleBoatPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPickItemPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPingRequestPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPlaceRecipePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPlayerAbilitiesPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPlayerActionPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPlayerCommandPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPlayerInputPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerPongPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerRecipeBookChangeSettingsPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerRecipeBookSeenRecipePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerRenameItemPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerResourcePackPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSeenAdvancementsPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSelectTradePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSetBeaconPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSetCarriedItemPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSetCommandBlockPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSetCommandMinecartPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSetCreativeModeSlotPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSetJigsawBlockPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSetStructureBlockPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSignUpdatePacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerSwingPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerTeleportToEntityPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerUseItemOnPacket.class
		);
		this.packetRegistry.registerAutoId(
			 State.GAME,
			 ServerUseItemPacket.class
		);

	}

	public PacketRegistry getPacketRegistry() {
		return packetRegistry;
	}
}
