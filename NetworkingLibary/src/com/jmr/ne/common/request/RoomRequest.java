package com.jmr.ne.common.request;

import com.jmr.ne.common.NEObject.NEObject;

public abstract class RoomRequest extends BaseRequest {
	
	/** Sets the zone variable with the given name
	 * @param obj Object to set the zone variable to.
	 * @param zone The name of the zone.
	 */
	protected void setZoneName(NEObject obj, String zone) {
		obj.put("zone", zone);
	}
	
	/** Sets the room variable with the given name
	 * @param obj Object to set the room variable to.
	 * @param room The name of the room.
	 */
	protected void setRoomName(NEObject obj, String room) {
		obj.put("room", room);
	}
	
}
