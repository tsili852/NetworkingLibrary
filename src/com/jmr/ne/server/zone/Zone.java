package com.jmr.ne.server.zone;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jmr.ne.common.exceptions.NEAlreadyInUserManagerException;
import com.jmr.ne.common.exceptions.NECanNotDeleteRoomException;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.exceptions.NEGuestsNotAllowedException;
import com.jmr.ne.common.exceptions.NEMaxRoomLimitException;
import com.jmr.ne.common.exceptions.NEMaxUserLimitException;
import com.jmr.ne.common.exceptions.NERoomDoesNotExistException;
import com.jmr.ne.common.exceptions.NERoomExistsException;
import com.jmr.ne.common.exceptions.NEUserDoesNotExistException;
import com.jmr.ne.common.exceptions.NEZoneExistsException;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.IUserController;
import com.jmr.ne.common.user.User;
import com.jmr.ne.common.user.UserManager;
import com.jmr.ne.server.database.Database;
import com.jmr.ne.server.module.ModuleLoader;
import com.jmr.ne.server.module.NEServerModule;
import com.jmr.ne.server.room.Room;

/**
 * Networking Library
 * Zone.java
 * Purpose: Manages all users in the zone, holds all rooms, allows a connection 
 * to a database, allows use of external modules, and sends all incoming events 
 * to the room handlers.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class Zone implements IUserController {
	
	/** Name of the zone. */
	private String name;

	/** List of rooms connected to the zone. */
	private transient HashMap<String, Room> roomList = new HashMap<String, Room>();
	
	/** List of users inside the Zone. */
	private transient final UserManager userManager = new UserManager();
	
	/** A module that is run in the zone. */
	protected transient NEServerModule module;
	
	/** The zone settings. */
	private ZoneSettings config = new ZoneSettings();

	/** The ID of the zone. */
	private transient int id = -1;
	
	/** The database connected to the zone. */
	private Database database;
	
	/** Creates a new zone and adds it to the ZoneManager.
	 * @param name The name of the zone.
	 */
	public Zone(String name) {
		this.name = name;
		try {
			ZoneManager.getInstance().addZone(this);
		} catch (NEZoneExistsException e) {
			e.printStackTrace();
		}
	}
	
	/** @return the module connected to the zone. */
	public NEServerModule getModule() {
		return this.module;
	}
	
	/** Loads an external module from the 'modules' folder to be used in the zone.
	 * @param modulePath The path to the module file with '.jar' included.
	 * @param classPath The path to the class File that is the start of the module.
	*/
	public void loadModule(String modulePath, String classPath) throws NEException {
		module = ModuleLoader.getInstance().loadModule(new File(modulePath), classPath);

		if (module != null) {
			if (!(module instanceof NEServerModule)) {
				module = null;
				throw new NEException("You can only load Server Modules to Rooms and Zones.");
			}
			module.init();
		}
	}
	
	@Override
	public ArrayList<User> getUsers() {
		return userManager.getUserArray();
	}
	
	/** @return The ID of the zone. */
	public int getId() {
		return id;
	}

	/** Sets the ID of the zone.
	 * @param id The id.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/** @return The name of the zone. */
	public String getName() {
		return name;
	}

	/** Sets the name of the zone.
	 * @param name The new name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Sets the database connected to the zone.
	 * @param database The database.
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	/** @return The database connected to the zone. */
	public Database getDatabase() {
		return database;
	}
	
	/** 
	 * @param roomName Name of the room to find.
	 * @return The room of the requested name
	 */
	public Room getRoomByName(String roomName) {
		return roomList.get(roomName);
	}

	/** @return The hashmap of rooms. */
	public HashMap<String, Room> getRoomList() {
		return roomList;
	}
	
	/** Adds a room to the zone.
	 * @param room The room.
	 * @throws NEException Room already exists in zone.
	 */
	public void addRoom(Room room) throws NERoomExistsException, NEMaxRoomLimitException {
		if(roomList.containsKey(room.getName())) {
			throw new NERoomExistsException();
		} else if (roomList.size() >= config.maxRooms) {
			throw new NEMaxRoomLimitException();
		} else {
			roomList.put(room.getName(), room);
			room.setParentZone(this);
		}
	}
	
	/** Removes a room from the zone.
	 * @param room The room to remove.
	 * @throws NERoomDoesNotExistException Room does not exist in the zone.
	 * @throws NECanNotDeleteRoomException Room is not dynamic and therefore can not be deleted. Can be changed in RoomSettings.
	 */
	public void removeRoom(Room room) throws NERoomDoesNotExistException, NECanNotDeleteRoomException {
		if(room.isDynamic()) {
			if(roomList.containsKey(room.getName())) {
				roomList.remove(room.getName());
			} else {
				throw new NERoomDoesNotExistException();
			}
		} else {
			throw new NECanNotDeleteRoomException();
		}
	}

	/** Calls the module event handler and room event handlers when a new packet is received.
	 * @param user The user the event came from.
	 * @param packet The packet that was sent. 
	 */
	public void callEvent(User user, Packet packet) {
		if (module != null)
			module.getEventHandler().callEvent(user, packet);
		Iterator it = roomList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pairs = (Map.Entry)it.next();
	    	Room room = (Room) pairs.getValue();
	    	room.callEvent(user, packet);
	    }
	}

	@Override
	public UserManager getUserManager() {
		return userManager;
	}

	/** @return The zone settings. */
	public ZoneSettings getConfig() {
		return config;
	}

	/** Sets the zone settings. */
	public void setConfig(ZoneSettings config) {
		this.config = config;
	}
	
	@Override
	public void addUser(User user) throws NEGuestsNotAllowedException, NEMaxUserLimitException, NEAlreadyInUserManagerException {
		if(getUsers().size() >= config.maxUsers) {
			throw new NEMaxUserLimitException();
		}
		
		if(!getConfig().allowGuestUser && user.isGuest()) {
			throw new NEGuestsNotAllowedException();
		}
		
		try {
			if(userManager.getUserByName(user.getUsername()) != null) {
				throw new NEAlreadyInUserManagerException("User already exists in zone.");
			}
		} catch (NEUserDoesNotExistException e) {
			//continue;
		}
		
		try {
			userManager.addUser(user);
		} catch (NEUserDoesNotExistException e) {
			e.printStackTrace();
		}
		user.setCurrentZone(this);
	}
	
	@Override
	public void removeUser(User user) {
		userManager.removeUser(user);
		user.setCurrentZone(null);
	}
	
}
