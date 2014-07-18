package com.jmr.ne.module;

import com.jmr.ne.common.event.EventHandler;
import com.jmr.ne.common.event.IEventListener;


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
