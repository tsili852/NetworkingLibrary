package com.jmr.ne.common.exceptions;


public class NECanNotDeleteRoomException extends NEException {

	public NECanNotDeleteRoomException() {
		super("Can't delete non-dynamic rooms");
	}

}
