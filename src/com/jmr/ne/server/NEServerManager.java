package com.jmr.ne.server;

import com.jmr.wrapper.server.Server;

public abstract class NEServerManager {

	/** Server object that handles the whole server. */
	protected Server server;
	
	/** Listener that waits and handles packets. */
	protected ServerListener listener;
	
	/** Manager that starts server and registers class .
	 * @param tcp The TCP port to be binded to.
	 * @param udp The UDP port to be binded to.
	 */
	public NEServerManager(int tcp, int udp) {
		server = new Server(tcp, udp);
		listener = new ServerListener();
		server.setListener(listener);
	}

	/** @returns instance of the listener */
	public ServerListener getListener() {
		return listener;
	}
	
}
