package com.jmr.ne.common.user;

import java.io.Serializable;
import java.util.ArrayList;

import com.jmr.ne.common.exceptions.NEAlreadyInUserManagerException;
import com.jmr.ne.common.exceptions.NEUserDoesNotExistException;
import com.jmr.wrapper.common.Connection;

/**
 * Networking Library
 * UserManager.java
 * Purpose: Holds all users of a system. Includes get methods to find users and allows 
 * you to add new users. 
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public class UserManager implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** Contains all of the users in an Array. */
	private final ArrayList<User> userList = new ArrayList<User>();
	
	/** Default Constructor. */
	public UserManager() {
	}
	
	/** Returns a User object given the connection. Creates a new User if it doesn't exist.
	 * @param con The connection of the client.
	 * @return the client's User object.
	 */
	public User getOrCreateUser(Connection con) {
		User user;
		try {
			user = getUserByCon(con);
		} catch (NEUserDoesNotExistException e) {
			user = new User(con);
			userList.add(user);
		}
		return user;
	}
	
	/** Returns a User object given the connection.
	 * @param con The connection of the client.
	 * @return the client's User object.
	 * @throws NEUserDoesNotExistException Thrown if the user does not exist.
	 */
	public User getUserByCon(Connection con) throws NEUserDoesNotExistException {
		for (User user : userList) {
	    	if (user.getSession().getConnection().equals(con)) {
	    		return user;
	    	}
		}
	    throw new NEUserDoesNotExistException("User does not exist in user manager.");
	}
	
	/** Returns a User object given the id.
	 * @param id the ID of the client.
	 * @return the client's User object.
	 * @throws NEUserDoesNotExistException Thrown if the user does not exist.
	 */
	public User getUserById(int id) throws NEUserDoesNotExistException {
		for (User user : userList)
	    	if (user.getId() == id)
	    		return user;
	    throw new NEUserDoesNotExistException("User does not exist in user manager.");
	}
	
	/** Returns a User object given the username.
	 * @param name The username of the client.
	 * @return the client's User object.
	 * @throws NEUserDoesNotExistException Thrown if the user does not exist.
	 */
	public User getUserByName(String name) throws NEUserDoesNotExistException {
		for (User user : userList) {
	    	if (user.getUsername().equalsIgnoreCase(name))
	    		return user;
		}
	    throw new NEUserDoesNotExistException("User does not exist in user manager.");
	}
	
	
	/** Adds a User to the user list if it doesnt exist already.
	 * @param user The User object.
	 * @throws NEAlreadyInUserManagerException Thrown if username exists in the list.
	 * @throws NEUserDoesNotExistException  Thrown if the connection already exists in the list.
	 */
	public void addUser(User user) throws NEAlreadyInUserManagerException, NEUserDoesNotExistException {
		try {
			if(getUserByName(user.getUsername()) != null) {
				throw new NEAlreadyInUserManagerException("User already exists.");
			}
		} catch (NEUserDoesNotExistException e) {
			//continue;
		}
		userList.add(user);
	}
	
	/** Removes a user from the user list.
	 * @param con The Connection object of the client.
	 */
	public void removeUser(Connection con) {
		try {
			User user = getUserByCon(con);
			userList.remove(user);
		} catch (NEUserDoesNotExistException e) {
			e.printStackTrace();
		}
	}
	
	/** Removes a user from the user list.
	 * @param user The User object.
	 */
	public void removeUser(User user) {
		userList.remove(user);
	}

	/** @return The userList array. */
	public ArrayList<User> getUserArray() {
		return userList;
	}
	
}
