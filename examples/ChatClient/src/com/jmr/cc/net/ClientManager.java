package com.jmr.cc.net;

import com.jmr.cc.net.events.JoinRoomEvent;
import com.jmr.cc.net.events.JoinZoneErrorEvent;
import com.jmr.cc.net.events.JoinZoneEvent;
import com.jmr.cc.net.events.NewMessageEvent;
import com.jmr.cc.net.events.NewUserEvent;
import com.jmr.cc.net.events.UserLeftEvent;
import com.jmr.ne.client.NEClientManager;
import com.jmr.wrapper.encryption.BasicEncryptor;

public class ClientManager extends NEClientManager {

	public ClientManager() {
		super("localhost", 9447, 9447);
		
		client.setEncryptionMethod(new BasicEncryptor());
		
		addEventListener(new JoinZoneErrorEvent());
		addEventListener(new JoinRoomEvent());
		addEventListener(new NewUserEvent());
		addEventListener(new JoinZoneEvent());
		addEventListener(new UserLeftEvent());
		addEventListener(new NewMessageEvent());
	}

}
