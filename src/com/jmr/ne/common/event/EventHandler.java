package com.jmr.ne.common.event;

import java.util.ArrayList;

import com.jmr.ne.common.exceptions.NEUserDoesNotExistException;
import com.jmr.ne.common.packet.NEPacket;
import com.jmr.ne.common.packet.Packet;
import com.jmr.ne.common.user.User;
import com.jmr.ne.server.room.Room;

public class EventHandler {

	/** Array that holds all registered event listeners */
	private ArrayList<IEventListener> listeners = new ArrayList<IEventListener>();
	
	/** Handles all listeners and waits for them to be called. */
	public EventHandler() {
	}
	
	/** Adds a listener to the array.
	 * @param eventListener Listener to be added.
	 */
	public void addListener(IEventListener eventListener) {
		listeners.add(eventListener);
	}
	
	/** Removes a listener from the array.
	 * @param eventListener Listener to removed.
	 */
	public void removeListener(IEventListener eventListener) {
		listeners.remove(eventListener);
	}
	
	/** Removes a listener from the array.
	 * @param name The name of the listener to removed.
	 */
	public void removeListener(String name) {
		for (int i = 0; i < listeners.size(); i++)
			if (listeners.get(i).getListeningPacketName().equalsIgnoreCase(name))
				listeners.remove(i);
	}
	
	/** Calls all event listeners waiting for the given event.
	 * @param user Instance of the user the event came from.
	 * @param packet The packet that was sent.
	 */
	public void callEvent(User user, Packet packet) {
		if (packet instanceof NEPacket)
			checkForUser(user, (NEPacket) packet);
		ArrayList<IEventListener> listeners = getListeners(packet.name);
		for (IEventListener el : listeners)
			el.handlePacket(user, packet);
	}
	
	/** A filter that checks if the NEObject contains a key called "user". If it does it gets the localized user object and sets the key to it.
	 * @param user The user that sent the packet.
	 * @param packet The packet sent.
	 */
	private void checkForUser(User user, NEPacket packet) {
		if (user.getLastRoomJoined() != null) {
			Room r = user.getLastRoomJoined();
			if (packet.vars.containsKey("room") && packet.vars.getObject("room") instanceof String) {
				r = user.getJoinedRoomByName(packet.vars.getString("room"));
			}
			if (packet.vars.containsKey("user") && packet.vars.getObject("user") instanceof String) {
				try {
					User u = r.getUserManager().getUserByName(packet.vars.getString("user"));
					packet.vars.put("user", u);
				} catch (NEUserDoesNotExistException e) {
					System.out.println("Username sent in packet does not exist in the user's room. Packet: " + packet.name);
					e.printStackTrace();
				}
			} else if (packet.vars.containsKey("user") && packet.vars.getObject("user") instanceof User) {
				try {
					User u1 = (User)packet.vars.getObject("user");
					User u = r.getUserManager().getUserByName(u1.getUsername());
					packet.vars.put("user", u);
				} catch (NEUserDoesNotExistException e) {
					System.out.println("Username sent in packet does not exist in the user's room. Packet: " + packet.name);
					e.printStackTrace();
				}
			}
		}
	}
	
	/** Gets all listeners that are waiting for the given event.
	 * @param name Name of the event that packets are waiting for.
	 * @return Returns all listeners waiting for the event.
	 */
	private ArrayList<IEventListener> getListeners(String name) {
		ArrayList<IEventListener> ret = new ArrayList<IEventListener>();
		for (IEventListener el : listeners) 
			if (el.getListeningPacketName().equalsIgnoreCase(name))
				ret.add(el);
		return ret;
	}
	
	/**
	 * Removes all listeners
	 */
	public void clearAllListeners() {
		listeners.clear();
	}
	
}
