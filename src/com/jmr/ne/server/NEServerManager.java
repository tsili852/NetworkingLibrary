package com.jmr.ne.server;

import com.jmr.wrapper.common.exceptions.NECantStartServer;
import com.jmr.wrapper.server.Server;

/**
 * Networking Library
 * NEServerManager.java
 * Purpose: Starts the server and manages the server listener to wait for events.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public abstract class NEServerManager {

	/** Server object that handles the whole server. */
	protected Server server;
	
	/** Listener that waits and handles packets. */
	protected ServerListener listener;
	
	/** Manager that starts server and registers class .
	 * @param tcp The TCP port to be binded to.
	 * @param udp The UDP port to be binded to.
	 * @throws NECantStartServer Thrown when the server can't be binded to the desired ports.
	 */
	public NEServerManager(int tcp, int udp) throws NECantStartServer {
		server = new Server(tcp, udp);
		listener = new ServerListener();
		server.setListener(listener);
	}

	/** @returns instance of the listener. */
	public ServerListener getListener() {
		return listener;
	}
	
}
