package net.cakemc.proxy.protocol.impl.packets.client.impl;

import io.netty.buffer.ByteBuf;

import net.cakemc.mc.lib.network.AbstractPacket;

public class ClientPlayerChatPacket extends AbstractPacket {
	//private UUID sender;
	//private int index;
	//private byte[] messageSignature;
	//private String content;
	//private long timeStamp;
	//private long salt;
	//private List<MessageSignature> lastSeenMessages;
	//private Component unsignedContent;
	//private ChatFilterType filterMask;
	//private Holder<ChatType> chatType;
	//private Component name;
	//private Component targetName;

	@Override
	public void read(ByteBuf buf) {
		//this.sender = readUUID(buf);
		//this.index = readVarInt(buf);
		//this.messageSignature = readNullable(buf, readComponent -> {
		//	byte[] signature = new byte[256];
		//	buf.readBytes(signature);
		//	return signature;
		//});
		//
		//this.content = readString(buf, 256);
		//this.timeStamp = buf.readLong();
		//this.salt = buf.readLong();
		//
		//this.lastSeenMessages = new ArrayList<>();
		//int seenMessageCount = Math.min(readVarInt(buf), 20);
		//for (int i = 0 ; i < seenMessageCount ; i++) {
		//	this.lastSeenMessages.add(MessageSignature.read(buf, helper));
		//}
		//
		//this.unsignedContent = readNullable(buf, helper::readComponent);
		//this.filterMask = ChatFilterType.from(readVarInt(buf));
		//this.chatType = readHolder(buf, helper::readChatType);
		//this.name = readComponent(buf);
		//this.targetName = readNullable(buf, helper::readComponent);
	}

	@Override
	public void write(ByteBuf buf) {
		//writeUUID(buf, this.sender);
		//writeVarInt(buf, this.index);
		//writeNullable(buf, this.messageSignature, ByteBuf::writeBytes);
		//
		//writeString(buf, this.content);
		//buf.writeLong(this.timeStamp);
		//buf.writeLong(this.salt);
		//
		//writeVarInt(buf, this.lastSeenMessages.size());
		//for (MessageSignature messageSignature : this.lastSeenMessages) {
		//	writeVarInt(buf, messageSignature.getId() + 1);
		//	if (messageSignature.getMessageSignature() != null) {
		//		buf.writeBytes(messageSignature.getMessageSignature());
		//	}
		//}
		//
		//writeNullable(buf, this.unsignedContent, helper::writeComponent);
		//writeVarInt(buf, this.filterMask.ordinal());
		//writeHolder(buf, this.chatType, helper::writeChatType);
		//writeComponent(buf, this.name);
		//writeNullable(buf, this.targetName, helper::writeComponent);
	}

}

