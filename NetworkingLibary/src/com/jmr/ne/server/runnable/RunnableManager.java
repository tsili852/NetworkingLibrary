package com.jmr.ne.server.runnable;

import java.util.ArrayList;

public class RunnableManager {
	
	/** Instance of the RunnableManager. Is a Singleton. */
	private static final RunnableManager instance = new RunnableManager();
	
	/** List of the Server Runnables. */
	private final ArrayList<AbstractServerRunnable> runnables = new ArrayList<AbstractServerRunnable>();
	
	/** Private constructor so it can't be instantiated again. */
	private RunnableManager() {
		
	}

	/** Adds a runnable to the list.
	 * @param asr The runnable.
	 */
	public void addRunnable(AbstractServerRunnable asr) {
		runnables.add(asr);
	}
	
	/** Removes a runnable from the list. 
	 * @param asr The runnable.
	 */
	public void removeRunnable(AbstractServerRunnable asr) {
		asr.stopThread();
		runnables.remove(asr);
	}
	
	/** @return The instance of the RunnableManager. */
	public static RunnableManager getInstance() {
		return instance;
	}
	
}
