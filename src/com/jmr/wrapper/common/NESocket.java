package com.jmr.wrapper.common;

import java.net.DatagramSocket;

public interface NESocket {

	/** Executes a new thread.
	 * @param run The thread.
	 */
	void executeThread(Runnable run);
	
	/** @return The listener object. */
	IListener getListener();
	
	/** @return The UDP port. */
	int getUdpPort();
	
	/** @return The UDP socket. */
	DatagramSocket getUdpSocket();
	
	/** @return Whether it is connected. */
	boolean isConnected();
	
	/** Closes the socket. */
	void close();
	
}
