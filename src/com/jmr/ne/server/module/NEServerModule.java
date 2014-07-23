package com.jmr.ne.server.module;

import com.jmr.ne.common.event.EventHandler;
import com.jmr.ne.common.event.IEventListener;

/**
 * Networking Library
 * NEServerModule.java
 * Purpose: The abstract class that all modules extend. Provides the methods needed to add event
 * listeners and custom code.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public abstract class NEServerModule {
	
	/** Handles and calls events as packages come in. */
	protected EventHandler eventHandler = new EventHandler();
	
	/** The start of the module. */
	public abstract void init();
	
	/** Add event listeners to the event handler.
	 * @param el The event listener.
	 */
	public void addEventListener(IEventListener el) {
		getEventHandler().addListener(el);
	}

	/** @return The event handler. */
	public EventHandler getEventHandler() {
		return eventHandler;
	}
	
	/** Removes an event listener from the event handler.
	 * @param el The event listener.
	 */
	public void removeEventListener(IEventListener el) {
		getEventHandler().removeListener(el);
	}
	
	/** Removes all event listeners from the event handler. */
	public void clearEventListeners() {
		eventHandler.clearAllListeners();
	}
	
}
