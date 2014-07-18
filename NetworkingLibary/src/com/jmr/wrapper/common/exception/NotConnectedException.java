package com.jmr.wrapper.common.exception;

public class NotConnectedException extends Exception {

	public NotConnectedException() {
		super("Client is not connected through TCP.");
	}
	
}
