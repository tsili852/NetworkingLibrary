package com.jmr.wrapper.client.threads;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.jmr.wrapper.client.Client;
import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.server.threads.DisconnectedThread;
import com.jmr.wrapper.server.threads.ReceivedThread;

public class TcpSocketReadThread implements Runnable {

	/** Instance of the client object. */
	private final Client client;
	
	/** Instance of the connection to the server. */
	private final Connection serverConnection;
	
	/** Creates a new thread to wait for incoming TCP packets.
	 * @param client Instance of the client.
	 * @param serverConnection Instance of the server connection.
	 */
	public TcpSocketReadThread(Client client, Connection serverConnection) {
		this.client = client;
		this.serverConnection = serverConnection;
	}
	
	@Override
	public void run() {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(serverConnection.getSocket().getInputStream());
		} catch (IOException e1) {
			client.executeThread(new DisconnectedThread(client.getListener(), serverConnection));
		}
		while (!serverConnection.getSocket().isClosed() && in != null) {
			try {
				Object object = null;
				if ((object = in.readObject()) != null && in != null) {					
					client.executeThread(new ReceivedThread(client.getListener(), serverConnection, object));
				}
			} catch (IOException | ClassNotFoundException e) {
				in = null;
				serverConnection.close();
				e.printStackTrace();
			}
		}
	}

}
