package com.jmr.wrapper.server.threads;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

public class NewConnectionThread implements Runnable {
	
	private final IListener listener;
	private final Connection con;
	
	public NewConnectionThread(IListener listener, Connection con) {
		this.listener = listener;
		this.con = con;
	}
	
	@Override
	public void run() {
		listener.connected(con);
	}

}
