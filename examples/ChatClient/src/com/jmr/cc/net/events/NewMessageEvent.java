package com.jmr.cc.net.events;

import com.jmr.cc.Main;
import com.jmr.cc.frames.ChatRoomFrame;
import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;

public class NewMessageEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return "newMessage";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		User u = (User) ne.vars.getObject("user");
		String message = ne.vars.getString("message");
		if (Main.getInstance().getCurrentFrame() instanceof ChatRoomFrame) {
			ChatRoomFrame crf = (ChatRoomFrame) Main.getInstance().getCurrentFrame();
			crf.addMessage(u.getUsername(), message);
		}
	}

}
