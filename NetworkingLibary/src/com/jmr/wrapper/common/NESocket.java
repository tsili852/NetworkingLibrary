package com.jmr.wrapper.common;

import java.net.DatagramSocket;

public interface NESocket {

	void executeThread(Runnable run);
	
	IListener getListener();
	
	int getUdpPort();
	
	DatagramSocket getUdpSocket();
	
	void close();
	
}
