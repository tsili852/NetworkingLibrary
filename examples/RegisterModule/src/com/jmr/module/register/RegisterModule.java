package com.jmr.module.register;

import com.jmr.ne.server.module.NEServerModule;

public class RegisterModule extends NEServerModule {

	@Override
	public void init() {
		System.out.println("Loaded Register Module.");
		
		addEventListener(new RegisterUserEvent());
	}

}
