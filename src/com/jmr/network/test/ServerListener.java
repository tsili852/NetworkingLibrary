package com.jmr.network.test;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

public class ServerListener implements IListener {

	@Override
	public void received(Connection con, Object object) {
		//System.out.println("Got packet from " + con.getId() + " - " + object);
		if (object instanceof Test) {
			Test test = (Test) object;
			System.out.println(test.getName());
		}
		
	}

	@Override
	public void connected(Connection con) {
		System.out.println("Connected: " + con.getId());
		//for (int i = 0; i < 10; i++)
			//con.sendUdp("Hello There " + i);
	}

	@Override
	public void disconnected(Connection con) {
		System.out.println("Disconnected: " + con.getId());
	}

}
