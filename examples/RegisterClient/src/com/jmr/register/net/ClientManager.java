package com.jmr.register.net;

import com.jmr.ne.client.NEClientManager;
import com.jmr.register.net.events.JoinZoneEvent;
import com.jmr.register.net.events.RegisterErrorEvent;
import com.jmr.register.net.events.RegisterSuccessEvent;


public class ClientManager extends NEClientManager {

	public ClientManager() {
		super("localhost", 9447, 9447);
		
		addEventListener(new JoinZoneEvent());
		addEventListener(new RegisterErrorEvent());
		addEventListener(new RegisterSuccessEvent());
	}
	
}
