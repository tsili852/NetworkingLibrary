package com.jmr.cc.net.events;

import com.jmr.ne.client.requests.JoinRoomRequest;
import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;

public class JoinZoneEvent implements IEventListener {

	@Override
	public String getListeningPacketName() {
		return NEEvent.ON_ZONE_JOIN.toString();
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		JoinRoomRequest jrr = new JoinRoomRequest("ChatRoom");
		try {
			jrr.send(user.getSession().getConnection());
		} catch (NEException e) {
			e.printStackTrace();
		}
	}

}
