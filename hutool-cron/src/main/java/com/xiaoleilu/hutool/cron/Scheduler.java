package com.xiaoleilu.hutool.cron;

import java.util.TimeZone;
import java.util.UUID;

import com.xiaoleilu.hutool.cron.task.RunnableTask;
import com.xiaoleilu.hutool.cron.task.Task;
import com.xiaoleilu.hutool.cron.task.TaskTable;
import com.xiaoleilu.hutool.util.ThreadUtil;

/**
 * 任务调度器
 * @author Looly
 *
 */
public class Scheduler {
	
	private TimeZone timezone;
	protected boolean daemon;
	private boolean started = false;
	private Object lock = new Object();
	
	protected TaskTable tasks = new TaskTable();
	private CronTimer timer;
	
	/**
	 * 新增Task，使用随机UUID
	 * @param pattern {@link CronPattern}对应的String表达式
	 * @param task {@link Runnable}
	 * @return ID
	 */
	public String schedule(String pattern, Runnable task){
		return schedule(pattern, new RunnableTask(task));
	}
	
	/**
	 * 新增Task，使用随机UUID
	 * @param pattern {@link CronPattern}对应的String表达式
	 * @param task {@link Task}
	 * @return ID
	 */
	public String schedule(String pattern, Task task){
		String id = UUID.randomUUID().toString();
		schedule(id, pattern, task);
		return id;
	}
	
	/**
	 * 新增Task
	 * @param id ID，为每一个Task定义一个ID
	 * @param pattern {@link CronPattern}对应的String表达式
	 * @param task {@link Runnable}
	 * @return this
	 */
	public Scheduler schedule(String id, String pattern, Runnable task){
		return schedule(id, new CronPattern(pattern), new RunnableTask(task));
	}
	
	/**
	 * 新增Task
	 * @param id ID，为每一个Task定义一个ID
	 * @param pattern {@link CronPattern}对应的String表达式
	 * @param task {@link Task}
	 * @return this
	 */
	public Scheduler schedule(String id, String pattern, Task task){
		return schedule(id, new CronPattern(pattern), task);
	}
	
	/**
	 * 新增Task
	 * @param id ID，为每一个Task定义一个ID
	 * @param pattern {@link CronPattern}
	 * @param task {@link Task}
	 * @return this
	 */
	public Scheduler schedule(String id, CronPattern pattern, Task task){
		tasks.add(id, pattern, task);
		return this;
	}
	
	/**
	 * 移除Task
	 * @param id Task的ID
	 */
	public synchronized void deschedule(String id) throws IndexOutOfBoundsException {
		this.tasks.remove(id);
	}
	
	/**
	 * 启动
	 * @return this
	 */
	public Scheduler start(){
		synchronized (lock) {
			
			//Start CronTimer
			timer = new CronTimer(this);
			timer.setDaemon(this.daemon);
			timer.start();
			this.started = true;
		}
		return this;
	}
	
	/**
	 * 停止定时任务
	 * @return this
	 */
	public Scheduler stop(){
		synchronized (lock) {
			if (!started) {
				throw new IllegalStateException("Scheduler not started");
			}
			this.timer.interrupt();
			ThreadUtil.waitForDie(this.timer);
			
			started = false;
		}
		return this;
	}
	
	/**
	 * @return 是否已经启动
	 */
	public boolean isStarted(){
		return this.started;
	}
	
	/**
	 * 设置时区
	 * @param timezone 时区
	 * @return this
	 */
	public Scheduler setTimeZone(TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}
	
	/**
	 * 获得时区，默认为 {@link TimeZone#getDefault()}
	 * @return 时区
	 */
	public TimeZone getTimeZone() {
		return timezone != null ? timezone : TimeZone.getDefault();
	}
	
	/**
	 * 是否为守护线程<br>
	 * 默认非守护线程
	 * @param on <code>true</code>为守护线程，否则非守护线程
	 * @throws CronException
	 */
	public void setDaemon(boolean on) throws CronException {
		synchronized (lock) {
			if (started) {
				throw new CronException("Scheduler already started!");
			}
			this.daemon = on;
		}
	}
}
