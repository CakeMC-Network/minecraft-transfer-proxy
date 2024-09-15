package net.cakemc.proxy.protocol.api.player;

import net.cakemc.mc.lib.game.text.test.api.chat.BaseComponent;
import net.cakemc.mc.lib.network.AbstractPacket;

public interface PlayerConnection {

	void packetReceived(AbstractPacket abstractPacket);

	void sendPacket(AbstractPacket abstractPacket);

	void disconnect(BaseComponent reason);

	void handleLogin(AbstractPacket abstractPacket);

	void handleStatus(AbstractPacket abstractPacket);

	void handleConfiguration(AbstractPacket abstractPacket);

	void handleIntent(AbstractPacket abstractPacket);

	void sendConfiguration();


}
