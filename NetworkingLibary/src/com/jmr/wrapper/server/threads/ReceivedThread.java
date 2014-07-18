package com.jmr.wrapper.server.threads;

import com.jmr.wrapper.common.Connection;
import com.jmr.wrapper.common.IListener;

public class ReceivedThread implements Runnable {
	
	private final IListener listener;
	private final Connection con;
	private final Object object;
	
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
