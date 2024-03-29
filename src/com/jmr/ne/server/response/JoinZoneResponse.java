package com.jmr.ne.server.response;

import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.exceptions.NEUserDoesNotExistException;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.server.zone.ZoneManager;

/**
 * Networking Library
 * JoinZoneResponse.java
 * Purpose: Waits for a user join zone request and checks cases to see if they are available
 * to. If it does accept the request it will return a packet named NEEvent.ON_ZONE_JOIN to the 
 * user that requested it. Otherwise it will return a packet named NEEvent.ON_ZONE_JOIN_ERROR
 * with the error stored in the key: NEEvent.ERROR_MESSAGE.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class JoinZoneResponse extends RoomResponse {
	
	/** @return the name of the packet which is "joinZoneRequest". */
	@Override
	public String getListeningPacketName() {
		return "joinZoneRequest";
	}

	/** Handles the incoming packet. Adds the user to the zone and sets their username and password.
	 * @param user The User it was sent from.
	 * @param packet The packet that was sent.
	 */
	@Override
	public void handlePacket(User user, Packet packet) {
		System.out.println("Got zone");
		NEPacket ne = (NEPacket) packet;
		String zone = getZoneName(ne.vars);
		String username = ne.vars.getString("username");
		String password = ne.vars.getString("password");

		if (username == null || username.equalsIgnoreCase("")) {
			NEPacket errorPacket = createErrorPacket("Please enter a username!", NEEvent.ON_ZONE_JOIN_ERROR);
			user.sendTCP(errorPacket);
			return;
		}
		if (zone != null) {
			try {
				if(ZoneManager.getInstance().getZoneByName(zone).getUserManager().getUserByName(username) != null) {
					NEPacket errorPacket = createErrorPacket("A user with that name is already connected!", NEEvent.ON_ZONE_JOIN_ERROR);
					user.sendTCP(errorPacket);
					return;
				}
			} catch (NEUserDoesNotExistException e) {
				//user isnt in zone, good, continue
			}
			try {
				ZoneManager.getInstance().getZoneByName(zone).addUser(user);
			} catch (NEException e) { //Send message back to the client on why they didnt join.
				e.printStackTrace();
				NEPacket errorPacket = createErrorPacket("The zone does not exist!", NEEvent.ON_ZONE_JOIN_ERROR);
				user.sendTCP(errorPacket);
				return;
			}
		}

		Packet response = new Packet(NEEvent.ON_ZONE_JOIN.toString());
		user.setIsGuest(false);
		user.setUsername(username);
		user.setPassword(password);
		user.sendTCP(response);
	}

}
