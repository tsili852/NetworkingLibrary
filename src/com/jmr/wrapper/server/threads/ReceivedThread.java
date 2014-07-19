package com.jmr.wrapper.server.threads;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

public class ReceivedThread implements Runnable {
	
	/** Instance of the listener object. */
	private final IListener listener;
	
	/** Instance of the connection. */
	private final Connection con;
	
	/** The object that was sent. */
	private final Object object;
	
	/** Creates a new thread to call the received event.
	 * @param listener Instance of the listener object.
	 * @param con Instance of the connection.
	 */
	public ReceivedThread(IListener listener, Connection con, Object object) {
		this.listener = listener;
		this.con = con;
		this.object = object;
	}
	
	@Override
	public void run() {
		listener.received(con, object);
	}

}
