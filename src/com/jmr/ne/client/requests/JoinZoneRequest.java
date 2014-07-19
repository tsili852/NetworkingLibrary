package com.jmr.ne.client.requests;

import com.jmr.ne.common.exceptions.NEException;
import com.jmr.ne.common.request.RoomRequest;
import com.jmr.wrapper.common.Connection;

public class JoinZoneRequest extends RoomRequest {

	/** The zone name, username, and password requests. */
	private String zoneName, username, password;
	
	/** Asks the server to join a zone with the given name.
	 * @param zoneName The name of the zone.
	 * @param username The desired username.
	 * @param password The desired password.
	 */
	public JoinZoneRequest(String zoneName, String username, String password) {
		this.zoneName = zoneName;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String getName() {
		return "joinZoneRequest";
	}
	
	@Override
	public void validate() throws NEException {
		if (zoneName == null || zoneName.equalsIgnoreCase(""))
			throw new NEException("Please set the zone name.");
	}

	@Override
	public void execute(Connection con) {
		setZoneName(packet.vars, zoneName);
		packet.vars.put("username", username);
		packet.vars.put("password", password);
		con.sendTcp(packet);
	}

}
