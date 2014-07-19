package com.jmr.ne.common.user;

import java.io.Serializable;
import java.util.ArrayList;

import com.jmr.ne.common.NEObject.NEObject;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.server.room.Room;
import com.jmr.ne.server.zone.Zone;
import com.jmr.wrapper.common.Connection;

/**
 * Networking Library
 * User.java
 * Purpose: An object that holds all user information. The username, password, joined rooms, joined zone, and session information. Created automatically when a new connection to the server is made. 
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** Holds all user connection information */
	private transient Session session;
	
	/** User variables */
	private NEObject vars = new NEObject();
	
	/** The username of the User. Set to "Default" by default. */
 	private String username = "Default";
	
 	/** Whether the User is a guest on the server or not. */
 	private transient boolean isGuest = false;
 	
 	/** The password of the User. */
	private transient String password;
	
	/** All of the rooms that the User is currently in. */
	private ArrayList<Room> roomsJoined = new ArrayList<Room>();
	
	/** The last room that the User joined. */
	private Room lastRoomJoined;
	
	/** The Zone the User is currently in. */
	private transient Zone currentZone;
	
	/** Default Constructor */
	public User() {
		session = new Session();
	}
	
	/** Holds information about the user.
	 * @param con The connection of the user.
	 */
	public User(Connection con) {
		session = new Session(con);
	}
	
	/** Sends a packet over TCP.
	 * @param packet The packet to be sent.
	 */ 
	public void sendTCP(Packet packet) {
		session.sendTCP(packet);
	}
	
	/** Sends a packet over UDP.
	 * @param packet The packet to be sent.
	 */
	public void sendUDP(Packet packet) {
		session.sendUDP(packet);
	}

	/** Sets the username of the User.
	 * @param username The desired username. 
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/** Sets the password of the User.
	 * @param password The desired password. 
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/** @return The username of the User. */
	public String getUsername() {
		return username;
	}
	
	/** @return The password of the User. */
	public String getPassword() {
		return password;
	}
	
	/** @return The session ID of the User. */
	public int getId() {
		return session.getId();
	}

	/** @return The last room that the User joined. */
	public Room getLastRoomJoined() {
		return lastRoomJoined;
	}

	/** Sets the last room that the User joined.
	 * @param lastRoomJoined The room.
	 */
	public void setLastRoomJoined(Room lastRoomJoined) {
		this.lastRoomJoined = lastRoomJoined;
	}
	
	/** Joins a room.
	 * @param room The room to join.
	 */
	public void joinRoom(Room room) {
		Room r = getJoinedRoomByName(room.getName());
		if (r == null) {
			lastRoomJoined = room;
			roomsJoined.add(room);
		} else {
			System.out.println("User already joined the room " + room.getName());
		}
	}
	
	/** Leaves all currently joined rooms. */
	public void leaveAllRooms() {
		for (Room r : roomsJoined)
			r.removeUser(this);
	}

	/** Gets a room the user is in with the given name.
	 * @param name The name of the room.
	 * @return The room or null if not in the room.
	 */
	public Room getJoinedRoomByName(String name) {
		for (Room room : roomsJoined)
			if (room.getName().equalsIgnoreCase(name))
				return room;
		return null;
	}
	
	/** @return The user variables. */
	public NEObject getVars() {
		return vars;
	}

	/** Sets the user variables.
	 * @param vars The variables.
	 */
	public void setVars(NEObject vars) {
		this.vars = vars;
	}

	/** @return The zone the User is in. */
	public Zone getCurrentZone() {
		return currentZone;
	}

	/** Set the current zone of the User.
	 * @param currentZone The zone.
	 */
	public void setCurrentZone(Zone currentZone) {
		this.currentZone = currentZone;
	}

	/** @return The rooms the User has currently joined. */
	public ArrayList<Room> getRoomsJoined() {
		return roomsJoined;
	}	
	
	/** @return The user's session. */
	public Session getSession() {
		return session;
	}
	
	/** @return Wether the User is a guest. */
	public boolean isGuest() {
		return isGuest;
	}
	
	/** Sets whether the User is a guest.
	 * @param isGuest Guest or not.
	 */
	public void setIsGuest(boolean isGuest) {
		this.isGuest = isGuest;
	}
	
	/** Sets the session of the user.
	 * @param con The connection of the user.
	 */
	public void setSession(Connection con) {
		session = new Session(con);
	}
	
}
