package com.jmr.ne.server.runnable;


/**
 * Networking Library
 * AbstractServerRunnable.java
 * Purpose: An abstract thread that is continuously ran. Uses a method to get the delta and 
 * update at a continuous speed so the time between each update is the same.
 *
 * @author Jon R (Baseball435)
 * @version 1.0 7/19/2014
 */

public abstract class AbstractServerRunnable implements Runnable {

	/** FPS of the thread. */
	protected int fps = 60;
	
	/** The amount of frames. */ 
	protected int frameCount = 0;
	
	/** Instances of the thread. */
	private Thread threadInstance;
	
	/** Whether or not it's running. */
	private boolean running;

	/** Default constructor. Adds this object to the RunnableManager. */
	public AbstractServerRunnable() {
		RunnableManager.getInstance().addRunnable(this);
	}

	/** Starts the thread. */
	public void startThread() {
		threadInstance = new Thread(this);
		threadInstance.start();
		running = true;
	}

	/** Stops the thread. */
	public void stopThread() {
		running = false;
	}

	@Override
	public void run() {
		final double GAME_HERTZ = 30.0;
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		final int MAX_UPDATES_BEFORE_RENDER = 5;
		double lastUpdateTime = System.nanoTime();
		final double TARGET_FPS = 60;
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (running) {
			double now = System.nanoTime();
			int updateCount = 0;
			while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
				onUpdate();
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				updateCount++;
			}

			if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
				lastUpdateTime = now - TIME_BETWEEN_UPDATES;
			}

			int thisSecond = (int) (lastUpdateTime / 1000000000);
			if (thisSecond > lastSecondTime) {
				fps = frameCount;
				frameCount = 0;
				lastSecondTime = thisSecond;
			}

			while (now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
				Thread.yield();
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}

				now = System.nanoTime();
			}
		}
	}

	/** Called each frame to update. */
	public abstract void onUpdate();

}
