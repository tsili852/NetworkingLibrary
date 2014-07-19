package com.jmr.cc.net.events;

import com.jmr.cc.Main;
import com.jmr.cc.frames.ChatRoomFrame;
import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.server.room.Room;

public class JoinRoomEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return NEEvent.ON_ROOM_JOIN.toString();
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		Room r = (Room) ne.vars.getObject("room");
		Main.getInstance().showFrame(new ChatRoomFrame());
		ChatRoomFrame crf = (ChatRoomFrame) Main.getInstance().getCurrentFrame();
		if (r != null) {
			for (User u : r.getUsers()) {
				crf.addUserToList(u.getUsername());
			}
		}
	}

}
