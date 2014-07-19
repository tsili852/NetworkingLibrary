package com.jmr.ne.server.room;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import com.jmr.ne.common.NEObject.NEObject;
import com.jmr.ne.common.exceptions.NEAlreadyInUserManagerException;
import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.exceptions.NEGuestsNotAllowedException;
import com.jmr.ne.common.exceptions.NEMaxUserLimitException;
import com.jmr.ne.common.exceptions.NEUserDoesNotExistException;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.IUserController;
import com.jmr.ne.common.user.User;
import com.jmr.ne.common.user.UserManager;
import com.jmr.ne.server.database.Database;
import com.jmr.ne.server.module.ModuleLoader;
import com.jmr.ne.server.module.NEServerModule;
import com.jmr.ne.server.zone.Zone;

public class Room implements IUserController, Serializable{

	private static final long serialVersionUID = 1L;
	
	/** Name of the room. */
	private String name;

	/** A list of users in the room. */
	private final UserManager userManager = new UserManager();
	
	/** Zone that is the room's parent. */
	protected transient Zone parentZone;

	/** A module that is run in the room. */
	protected transient NEServerModule module;
	
	/** The Room variables. */
	protected NEObject vars = new NEObject();
	
	/** Connection to a database. */
	protected Database database;
	
	/** The settings to the room. */
	protected RoomSettings config;

	/** Default constructor. */
	public Room() {
	}
	
	/** Creates a new room.
	 * @param name Name of the room.
	 * @param parentZone Zone that the room is in. 
	 */
	public Room(String name, Zone parentZone) {
		this(name, parentZone, new RoomSettings());
	}
	
	/** Creates a new room.
	 * @param name Name of the room.
	 * @param parentZone Zone that the room is in. 
	 * @param roomSettings Custom settings of the room.
	 */
	public Room(String name, Zone parentZone, RoomSettings roomSettings) {
		this.name = name;
		this.parentZone = parentZone;
		this.config = roomSettings;
	}
	
	/** @return The name of the room. */
	public String getName() {
		return name;
	}

	/** Sets the name of the room.
	 * @param name The new name;
 	 */
	public void setName(String name) {
		this.name = name;
	}

	/** @return Whether the room can be deleted or not. */
	public boolean isDynamic() {
		return config.isDynamic;
	}

	/** Sets the database connected to the room.
	 * @param database The database.
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	/** @return The database connected to the room. */
	public Database getDatabase() {
		return database;
	}
	
	/** @return The module connected to the room. */
	public NEServerModule getModule() {
		return module;
	}
	
	@Override
	public ArrayList<User> getUsers() {
		return userManager.getUserArray();
	}

	@Override
	public void addUser(User user) throws NEAlreadyInUserManagerException, NEGuestsNotAllowedException, NEMaxUserLimitException, NEUserDoesNotExistException {

		if(getUsers().size() >= config.maxUsers) {
			throw new NEMaxUserLimitException();
		}
		
		if(!config.allowGuestUser && user.isGuest()) {
			throw new NEGuestsNotAllowedException();
		}
		
		try {
			if(userManager.getUserByName(user.getUsername()) != null) {
				throw new NEAlreadyInUserManagerException("User already exists in zone.");
			}
		} catch (NEUserDoesNotExistException e) {
			//continue
		}
		userManager.addUser(user);
		user.joinRoom(this);

	}
	
	@Override
	public void removeUser(User user) {
		userManager.removeUser(user);
		
		if(config.deleteOnEmpty) {
			try {
				getParentZone().removeRoom(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Calls an event from the module event handler.
	 * @param user The user the event was called from.
	 * @param packet The packet that was sent.
	 */
	public void callEvent(User user, Packet packet) {
		if (module != null) {
			module.getEventHandler().callEvent(user, packet);
		}
	}

	/** @return The parent zone. */
	public Zone getParentZone() {
		return parentZone;
	}

	/** Sets the parent zone.
	 * @param parentZone The zone.
	 */
	public void setParentZone(Zone parentZone) {
		this.parentZone = parentZone;
	}

	@Override
	public UserManager getUserManager() {
		return userManager;
	}

	/** @return The room settings. */
	public RoomSettings getConfig() {
		return config;
	}

	/** Sets the room settings.
	 * @param config The room settings.
	 */
	public void setConfig(RoomSettings config) {
		this.config = config;
	}
	
	/** @return The room variables. */
	public NEObject getVars() {
		return vars;
	}
	
	/** Loads an external module from the 'modules' folder to be used in the room.
	 * @param modulePath The path to the module file with '.jar' included.
	 * @param classPath The path to the class File that is the start of the module.
	*/
	public void loadModule(String modulePath, String classPath) throws NEException { 
		module = ModuleLoader.getInstance().loadModule(new File(modulePath), classPath);

		if (module != null) { 
			if (!(module instanceof NEServerModule)) {
				module = null;
				throw new NEException("You can only load Server Modules to Rooms.");
			}
			module.init(); 
		}
	}
		
}
