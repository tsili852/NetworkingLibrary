package com.jmr.ne.server.response;

import com.jmr.ne.common.NEObject.NEObject;

public abstract class RoomResponse extends BaseResponse {

	/** Returns the zone name from a NEObject from a NEPacket.
	 * @param obj The object to check.
	 * @return The zone name.
	 */
	protected String getZoneName(NEObject obj) {
		if (obj.containsKey("zone"))
			return obj.getString("zone");
		return null;
	}
	
	/** Returns the room name from a NEObject from a NEPacket.
	 * @param obj The object to check.
	 * @return The room name.
	 */
	protected String getRoomName(NEObject obj) {
		if (obj.containsKey("room"))
			return obj.getString("room");
		return null;
	}
	
}
