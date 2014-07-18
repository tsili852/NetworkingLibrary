package com.jmr.network.test;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

public class ClientListener implements IListener {

	@Override
	public void received(Connection con, Object object) {
		System.out.println("Got object from " + con.getId() + " - " + object);
	}

	@Override
	public void connected(Connection con) {
		
	}

	@Override
	public void disconnected(Connection con) {
		
	}

}
