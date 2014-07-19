package com.jmr.module.chat.responses;

import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.server.response.RoomResponse;
import com.jmr.ne.server.room.Room;
import com.jmr.ne.server.zone.Zone;
import com.jmr.ne.server.zone.ZoneManager;

public class MessageResponse extends RoomResponse {

	@Override
	public String getListeningPacketName() {
		return "messageRequest";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		String message = ne.vars.getString("message");
		
		NEPacket response = new NEPacket("newMessage");
		response.vars.put("user", user);
		response.vars.put("message", message);
		
		String zone = getZoneName(ne.vars);
		String room = getRoomName(ne.vars);
		Zone z = ZoneManager.getInstance().getZoneByName(zone);
		if (z != null) {
			Room r = z.getRoomByName(room);
			if (r != null) {
				for (User u : r.getUsers()) {
					u.sendTCP(response);
				}
			}
		}
	}

}
