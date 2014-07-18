package com.jmr.ne.server;

import java.util.Iterator;
import java.util.Map;

import com.jmr.ne.common.event.EventHandler;
import com.jmr.ne.common.event.NEEvent;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.common.user.UserManager;
import com.jmr.ne.server.response.JoinRoomResponse;
import com.jmr.ne.server.response.JoinZoneResponse;
import com.jmr.ne.server.room.Room;
import com.jmr.ne.server.zone.Zone;
import com.jmr.ne.server.zone.ZoneManager;
import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

public class ServerListener implements IListener {

	/** Handles all connections. */
	private static final UserManager userManager = new UserManager();
	
	/** The server event handler which handles global events. Should be used minimally. */
	private final EventHandler serverEventHandler = new EventHandler();
	
	/** Default constructor. Waits for packets to come in and passes them to the event handler.*/
	public ServerListener() {
		serverEventHandler.addListener(new JoinZoneResponse());
		serverEventHandler.addListener(new JoinRoomResponse());
	}
	
	/** Inherited method that waits for incoming packets.
	 * @param con The connection the received packet came from.
	 * @param object The object sent.
	 */
	@Override
	public void received (Connection con, Object object) {
		if (object instanceof Packet) {
			Packet packet = (Packet) object;
			User user = userManager.getOrCreateUser(con);

			serverEventHandler.callEvent(user, packet);
			
			if (packet instanceof NEPacket) {
				NEPacket ne = (NEPacket) packet;	
				String zoneName = null;
				if (user.getCurrentZone() != null)
					zoneName = user.getCurrentZone().getName();
				/** Check to see if being sent to specific zone or room */
				if (ne.vars.containsKey("zone"))
					 zoneName = ne.vars.getString("zone");
				
				if (zoneName != null && !zoneName.equalsIgnoreCase("")) {
					if (ne.vars.containsKey("room")) {
						String room = ne.vars.getString("room");
						ZoneManager.getInstance().getZoneByName(zoneName).getRoomByName(room).callEvent(user, packet);
						return;
					} else {
						ZoneManager.getInstance().getZoneByName(zoneName).callEvent(user, packet);
						return;
					}
				}
			}
			Iterator it = ZoneManager.getInstance().getZoneList().entrySet().iterator();
			while (it.hasNext()) {
			    Map.Entry pairs = (Map.Entry)it.next();
			    Zone zone = (Zone) pairs.getValue();
				zone.callEvent(user, packet);
			} 
		}
	}
	
	/** Inherited method called when a new client connects. 
	 * @param con The Connection object of the client
	 */
	@Override
	public void connected(Connection con) {
		User user = userManager.getOrCreateUser(con); //Creates the user
		NEPacket packet = new NEPacket(NEEvent.NEW_USER_CONNECTED.toString());
		packet.vars.put("user", user);
		
		if (user.getCurrentZone() != null)
			user.getCurrentZone().callEvent(user, packet);
		
		for(Room room : user.getRoomsJoined()) {
			room.callEvent(user, packet);
		}
	}
	
	/** Sends the NEEvent.USER_LEAVE_ROOM packet to all clients when a user disconnects.
	 * @param user The user disconnecting.
	 */
	private void disconnect(User user) {
		System.out.println("Disconnected: " +user.getUsername());
		NEPacket packet = new NEPacket();
		packet.name = NEEvent.USER_LEAVE_ROOM.toString();
		packet.vars.put("user", user);
		packet.vars.put("room", user.getLastRoomJoined().getName());
			
		for (Room r : user.getRoomsJoined()) {
			for (User u : r.getUsers())
				if (u != user)
					u.sendTCP(packet);
			r.removeUser(user);
		}
		user.getCurrentZone().removeUser(user);
	}
	
	/** Inherited method called when a client disconnects. 
	 * @param con The Connection object of the client
	 */
	@Override
	public void disconnected(Connection con) {
		User user = userManager.getOrCreateUser(con);
		if (user.getCurrentZone() != null && user.getLastRoomJoined() != null) {
			disconnect(user);
		}
		userManager.removeUser(con);
	}
	
	/** @return The server event handler. */
	public EventHandler getServerEventHandler() {
		return serverEventHandler;
	}
	
	/** @return The server user manager. */
	public static UserManager getServerUserManager() {
		return userManager;
	}
	
}
