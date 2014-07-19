package com.jmr.wrapper.server.threads;

import java.io.IOException;
import java.net.ServerSocket;

import com.jmr.wrapper.server.Server;

public class TcpReadThread implements Runnable {

	/** Instance of the Server's TCP Socket. */
	private final ServerSocket tcpSocket;
	
	/** Instance of the Server. */
	private final Server server;
	
	/** Waits for new sockets to connect. 
	 * @param server Instance of the server.
	 * @param tcpSocket Instance of the Server's TCP Socket. 
	 */
	public TcpReadThread(Server server, ServerSocket tcpSocket) {
		this.tcpSocket = tcpSocket;
		this.server = server;
	}
	
	@Override
	public void run() {
		while (!tcpSocket.isClosed()) {
			try {
				server.executeThread(new AcceptedSocketThread(server, tcpSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
