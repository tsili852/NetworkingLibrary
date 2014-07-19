package com.jmr.register.net.request;

import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.request.RoomRequest;
import com.jmr.wrapper.common.Connection;

public class RegisterRequest extends RoomRequest {

	private String username, password, email;
	
	public RegisterRequest(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	@Override
	public String getName() {
		return "registerUser";
	}

	@Override
	public void validate() throws NEException {
		
	}

	@Override
	public void execute(Connection con) {
		packet.vars.put("username", username);
		packet.vars.put("password", password);
		packet.vars.put("email", email);
		con.sendTcp(packet);
	}

}
