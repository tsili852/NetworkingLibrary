package com.jmr.ne.common.user;

import java.util.ArrayList;

import com.jmr.ne.common.exceptions.NEAlreadyInUserManagerException;
import com.jmr.ne.common.exceptions.NEGuestsNotAllowedException;
import com.jmr.ne.common.exceptions.NEMaxUserLimitException;
import com.jmr.ne.common.exceptions.NEUserDoesNotExistException;

public interface IUserController {

	/** @return The user array. */
	public ArrayList<User> getUsers();
	
	/** @return The user manager. */
	public UserManager getUserManager();
	
	/** Adds a user to the user manager.
	 * @param user The user to add.
	 * @throws NEAlreadyInUserManagerException User already exists in user manager.
	 * @throws NEGuestsNotAllowedException User is a guest and can't be added to room. Can be changed in RoomSettings.
	 * @throws NEMaxUserLimitException Room or zone is full.
	 */
	public void addUser(User user) throws NEGuestsNotAllowedException, NEMaxUserLimitException, NEAlreadyInUserManagerException, NEUserDoesNotExistException;
	
	/** Removes a user from the user manager.
	 * @param user The user to remove.
	 */
	public void removeUser(User user);
	
}
