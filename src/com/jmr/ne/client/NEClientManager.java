package com.jmr.ne.client;

import com.jmr.ne.common.NEObject.NEObject;
import com.jmr.ne.common.event.IEventListener;
import com.jmr.ne.common.user.User;
import com.jmr.wrapper.client.Client;

/**
 * Networking Library
 * NEClientManager.java
 * Purpose: Starts a new client and connects to the server.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public abstract class NEClientManager {

	/** IP Address to connect to. */
	private final String ip;
	
	/** UDP & TCP ports */
	private final int tcp, udp;
	
	/** Client that holds connection information */
	protected final Client client;
	
	/** Instance of the client's User object */
	protected static User myUser;
	
	/** Client-sided variables */
	protected NEObject vars = new NEObject();
	
	/** Listener that receives and handles incoming packets */
	protected final ClientListener listener;
	
	/** Takes care of registering classes and connection to the client
	 * @param ip The ip address to connect to.
	 * @param tcp The TCP port to be binded to.
	 * @param udp The UDP port to be binded to.
	 */
	public NEClientManager(String ip, int tcp, int udp) {
		this.client = new Client(ip, tcp, udp);
		this.ip = ip;
		this.tcp = tcp;
		this.udp = udp;
		
		listener = new ClientListener(this);
		client.setListener(listener);
	}
	
	/** Connects to the server and sets the client-side user with the server connection. */
	public void connect() {
		if (client.getServerConnection() == null) {
			client.connect();
			myUser = new User(client.getServerConnection());
		}
	}

	/** @returns instance of the listener. */
	public ClientListener getListener() {
		return listener;
	}
	
	/** Adds an event listener to the event handler.
	 * @param el The event listener object.
	 */
	public void addEventListener(IEventListener el) {
		listener.getEventHandler().addListener(el);
	}
	
	/** @return The client object */
	public Client getClient() {
		return client;
	}
	
	/** @return The client-sided variables */
	public NEObject getVars() {
		return vars;
	}
	
	/** @return Instance of the client User */
	public static User getMyUser() {
		return myUser;
	}
	
	/** Sets the instance of the client User object.
	 * @param user The user object.
	 */
	public void setMyUser(User user) {
		myUser = user;
		myUser.setSession(client.getServerConnection());
	}
	
}
