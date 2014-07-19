package com.jmr.wrapper.client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jmr.wrapper.client.threads.TcpSocketReadThread;
import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;
import com.jmr.wrapper.common.NESocket;
import com.jmr.wrapper.server.ConnectionManager;
import com.jmr.wrapper.server.threads.UdpReadThread;

public class Client implements NESocket {

	/** The TCP and UDP port. */
	private final int tcpPort, udpPort;
	
	/** The InetAddress of the server to connect to. */
	private InetAddress address;
	
	/** The executor for all threads. */
	private ExecutorService mainExecutor;
	
	/** The socket used to send packets over TCP. */
	private Socket tcpSocket;
	
	/** The socket used to send packets over UDP. */
	private DatagramSocket udpSocket;
	
	/** The listener object. */
	private IListener listener;
	
	/** The connection to the server. */
	private Connection serverConnection;
	
	/** Creates a new client sets the variables to be used to connect to a server later.
	 * @param address The address to the server.
	 * @param tcpPort The TCP port.
	 * @param udpPort The UDP port.
	 */
	public Client(String address, int tcpPort, int udpPort) {
		try {
			this.address = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
	}
	
	/** Connects to the server. */
	public void connect() {
		try {
			udpSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			tcpSocket = new Socket(address, tcpPort);
			tcpSocket.setSoLinger(true, 0);
			serverConnection = new Connection(udpPort, tcpSocket, udpSocket);
			ConnectionManager.getInstance().addConnection(serverConnection);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainExecutor = Executors.newCachedThreadPool();
		
		if (tcpSocket != null && tcpSocket.isConnected() && udpSocket != null) {
			mainExecutor.execute(new UdpReadThread(this, udpSocket));
			mainExecutor.execute(new TcpSocketReadThread(this, serverConnection));
			sendTcp("ConnectedToServer");
			sendUdp("SettingUdpPort");
		}
	}
	
	/** Sets the listener object.
	 * @param listener The listener.
	 */
	public void setListener(IListener listener) {
		this.listener = listener;
	}
	
	@Override
	public IListener getListener() {
		return listener;
	}
	
	@Override
	public boolean isConnected() {
		return udpSocket != null && tcpSocket != null && tcpSocket.isConnected();
	}
	
	@Override
	public void executeThread(Runnable run) {
		mainExecutor.execute(run);
	}
	
	@Override
	public int getUdpPort() {
		return udpPort;
	}
	
	@Override
	public void close() {
		try {
			tcpSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		udpSocket.close();
	}
	/** @return The connection to the server. */
	public Connection getServerConnection() {
		return serverConnection;
	}
	
	/** Sends a packet to the server over UDP
	 * @param object The object to send.
	 */
	public void sendUdp(Object object) {
		serverConnection.sendUdp(object);
	}
	
	/** Sends a packet to the server over TCP
	 * @param object The object to send.
	 */
	public void sendTcp(Object object) {
		serverConnection.sendTcp(object);
	}

	@Override
	public DatagramSocket getUdpSocket() {
		return udpSocket;
	}
	
}
