package com.vw.ide.server.servlet.processor.servant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.vw.ide.server.vwml.log.router.VWMLProcessorLogRouterAppender;

/**
 * Takes task and processes in separate thread
 * @author Oleg
 *
 */
public class Servant {
	
	/**
	 * Caller's implemented task
	 * @author Oleg
	 *
	 */
	public static abstract class Task {
		private String userName;
		private int id;
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public void process() {
			VWMLProcessorLogRouterAppender appender = (VWMLProcessorLogRouterAppender)Logger.getRootLogger().getAppender(VWMLProcessorLogRouterAppender.logRouterName);
			if (appender != null) {
				appender.getLogOutStream().setUserName(userName);
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + id;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Task other = (Task) obj;
			if (id != other.id)
				return false;
			return true;
		}
	}
	
	/**
	 * Every user has own task processor which is run in separate thread.
	 * This strategy will be changed on L_and_F strategy in future
	 * @author Oleg
	 *
	 */
	protected static class TaskProcessor implements Runnable {

		private ConcurrentLinkedQueue<Task> taskQueue = new ConcurrentLinkedQueue<Task>();
		private AtomicBoolean stopFlag = new AtomicBoolean();
		private Logger logger = Logger.getLogger(Servant.class);
		
		/**
		 * Starts task processor
		 */
		public void start() {
			Thread t = new Thread(this);
			t.start();
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
		
		public void stop() {
			clear();
			stopFlag.getAndSet(true);
			synchronized(stopFlag) {
				try {
					stopFlag.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void scheduleTask(Task task) {
			taskQueue.offer(task);
			synchronized(this) {
				this.notifyAll();
			}
		}
		
		public void removeTask(Task task) {
			taskQueue.remove(task);
		}
		
		public void clear() {
			taskQueue.clear();
		}
		
		@Override
		public void run() {
			synchronized(this) {
				notifyAll();
			}
			while(!stopFlag.get()) {
				if (taskQueue.size() == 0) {
					synchronized(this) {
						try {
							wait();
						} catch (InterruptedException e) {
						}
					}
				}
				else {
					Task task = taskQueue.poll();
					if (task != null) {
						if (logger.isInfoEnabled()) {
							logger.info("The task '" + task + "' for user '" + task.getUserName() + "' scheduled for processing");
						}
						task.process();
					}
				}
			}
			synchronized(stopFlag) {
				stopFlag.notifyAll();
			}
		}
	}
	
	private Logger logger = Logger.getLogger(Servant.class);
	private static Map<String, TaskProcessor> s_taskQueue = new ConcurrentHashMap<String, TaskProcessor>();
	private static Servant s_servant = new Servant();
	
	public static Servant instance() {
		return s_servant;
	}
	
	/**
	 * Schedules user's task
	 * @param userName
	 * @param task
	 */
	public void scheduleTask(String userName, Task task) {
		if (logger.isInfoEnabled()) {
			logger.info("Scheduling task '" + task + "' for user '" + userName + "'");
		}
		TaskProcessor tp = s_taskQueue.get(userName);
		if (tp == null) {
			tp = new TaskProcessor();
			tp.start();
			s_taskQueue.put(userName, tp);
		}
		task.setUserName(userName);
		tp.scheduleTask(task);
	}

	/**
	 * Removes user's task
	 * @param userName
	 * @param task
	 */
	public void removeTask(String userName, Task task) {
		TaskProcessor tp = s_taskQueue.get(userName);
		if (tp != null) {
			tp.removeTask(task);
		}
	}
	
	/**
	 * Stops user's task processor
	 * @param userName
	 */
	public void stop(String userName) {
		TaskProcessor tp = s_taskQueue.get(userName);
		if (tp != null) {
			tp.stop();
			s_taskQueue.remove(userName);
		}
	}
	
	/**
	 * Stops all task processors
	 */
	public void stopAll() {
		for(String userName : s_taskQueue.keySet()) {
			TaskProcessor tp = s_taskQueue.get(userName);
			if (tp != null) {
				tp.stop();
			}
		}
		s_taskQueue.clear();
	}
}
