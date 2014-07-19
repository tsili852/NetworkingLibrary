package com.jmr.wrapper.server.threads;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

public class NewConnectionThread implements Runnable {
	
	/** Instance of the listener object. */
	private final IListener listener;
	
	/** Instance of the connection. */
	private final Connection con;
	
	/** Creates a new thread to call the new connection event.
	 * @param listener Instance of the listener object.
	 * @param con Instance of the connection.
	 */
	public NewConnectionThread(IListener listener, Connection con) {
		this.listener = listener;
		this.con = con;
	}
	
	@Override
	public void run() {
		listener.connected(con);
	}

}
