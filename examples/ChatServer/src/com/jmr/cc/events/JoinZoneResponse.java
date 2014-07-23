package com.jmr.cc.events;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.exceptions.NEDatabaseQueryError;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.exceptions.NEUserDoesNotExistException;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.server.database.Database;
import com.jmr.ne.server.response.RoomResponse;
import com.jmr.ne.server.zone.Zone;
import com.jmr.ne.server.zone.ZoneManager;

public class JoinZoneResponse extends RoomResponse {

	@Override
	public String getListeningPacketName() {
		return "joinZoneRequest";
	}

	@Override
	public void handlePacket(User user, Packet packet) {
		NEPacket ne = (NEPacket) packet;
		String zone = getZoneName(ne.vars);
		String username = ne.vars.getString("username");
		String password = ne.vars.getString("password");
		
		if (username == null || username.equalsIgnoreCase("")) {
			NEPacket errorPacket = createErrorPacket("Please enter a username!", NEEvent.ON_ZONE_JOIN_ERROR);
			user.sendTCP(errorPacket);
			return;
		}
		
		if (zone.equalsIgnoreCase("ChatZone")) {
			Zone z = ZoneManager.getInstance().getZoneByName(zone);
			Database db = z.getDatabase();
			ResultSet rs = null;
			try {
				rs = db.executeQuery("SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'");
			} catch (NEDatabaseQueryError e) {
				e.printStackTrace();
			}

			boolean passed = false;
			try {
				if (db.getRowCount(rs) > 0) { //means there is a row with the username and pass given
					passed = true;
				}
			} catch (NEDatabaseQueryError e) {
				e.printStackTrace();
			}
			if (!passed) {
				NEPacket errorPacket = createErrorPacket("The username or password you provided is incorrect.", NEEvent.ON_ZONE_JOIN_ERROR);
				user.sendTCP(errorPacket);
				return;
			}
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
				NEPacket errorPacket = createErrorPacket("The zone does not exist!", NEEvent.ON_ZONE_JOIN_ERROR);
				user.sendTCP(errorPacket);
				return;
			}
		}

		Packet response = new Packet(NEEvent.ON_ZONE_JOIN.toString());
		user.setUsername(username);
		user.setPassword(password);
		user.sendTCP(response);
	}

}