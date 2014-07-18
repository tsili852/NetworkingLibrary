package com.jmr.wrapper.common;


public interface IListener {

	void received(Connection con, Object object);
	
	void connected(Connection con);
	
	void disconnected(Connection con);
	
}
