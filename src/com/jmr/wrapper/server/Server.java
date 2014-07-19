package com.jmr.wrapper.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jmr.wrapper.common.IListener;
import com.jmr.wrapper.common.NESocket;
import com.jmr.wrapper.server.threads.TcpReadThread;
import com.jmr.wrapper.server.threads.UdpReadThread;

public class Server implements NESocket {

	/** Used to manage and run threads. */
	private final ExecutorService mainExecutor;
	
	/** The UDP port of the server. */
	private final int udpPort;
	
	/** The TCP socket. */
	private ServerSocket tcpSocket;
	
	/** The UDP socket. */
	private DatagramSocket udpSocket;
	
	/** The listener object. */
	private IListener listener;
	
	/** Starts a new server on the TCP and UDP port.
	 * @param tcpPort The TCP port.
	 * @param udpPort The UDP port.
	 */
	public Server(int tcpPort, int udpPort) {
		try {
			tcpSocket = new ServerSocket(tcpPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			udpSocket = new DatagramSocket(new InetSocketAddress("localhost", udpPort) );
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.udpPort = udpPort;
		mainExecutor = Executors.newCachedThreadPool();
		if (tcpSocket != null && udpSocket != null) {
			mainExecutor.execute(new UdpReadThread(this, udpSocket));
			mainExecutor.execute(new TcpReadThread(this, tcpSocket));
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
		return udpSocket.isConnected() && tcpSocket != null;
	}
	
	@Override
	public int getUdpPort() {
		return udpPort;
	}
	
	@Override
	public void executeThread(Runnable run) {
		mainExecutor.execute(run);
	}
	
	@Override
	public DatagramSocket getUdpSocket() {
		return udpSocket;
	}
	
	@Override
	public void close() {
		try {
			tcpSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ConnectionManager.getInstance().close();
		udpSocket.close();
	}
	
}
