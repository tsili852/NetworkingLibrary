package com.jmr.module.chat;

import com.jmr.module.chat.responses.MessageResponse;
import com.jmr.ne.server.module.NEServerModule;

public class ChatModule extends NEServerModule {

	@Override
	public void init() {
		System.out.println("Chat Module loaded.");
		addEventListener(new MessageResponse());
	}

}
