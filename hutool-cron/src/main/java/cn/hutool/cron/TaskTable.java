package cn.hutool.cron;

import java.util.ArrayList;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.task.Task;

/**
 * 定时任务表<br>
 * 任务表将ID、表达式、任务一一对应，定时任务执行过程中，会周期性检查定时任务表中的所有任务表达式匹配情况，从而执行其对应的任务<br>
 * 任务的添加、移除使用读写锁保证线程安全性
 * 
 * @author Looly
 *
 */
public class TaskTable {
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private Scheduler scheduler;
	private TimeZone timezone;
	
	private ArrayList<String> ids = new ArrayList<>();
	private ArrayList<CronPattern> patterns = new ArrayList<>();
	private ArrayList<Task> tasks = new ArrayList<>();
	private int size;
	
	/**
	 * 构造
	 * 
	 * @param scheduler {@link Scheduler}
	 */
	public TaskTable(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.timezone = scheduler.getTimeZone();
	}
	
	/**
	 * 新增Task
	 * 
	 * @param id ID
	 * @param pattern {@link CronPattern}
	 * @param task {@link Task}
	 * @return this
	 */
	public TaskTable add(String id, CronPattern pattern, Task task){
		final Lock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			if(ids.contains(id)){
				throw new CronException("Id [{}] has been existed!", id);
			}
			ids.add(id);
			patterns.add(pattern);
			tasks.add(task);
			size++;
		} finally {
			writeLock.unlock();
		}
		return this;
	}
	
	/**
	 * 移除Task
	 * @param id Task的ID
	 */
	public void remove(String id) {
		final Lock writeLock = lock.writeLock();
		try {
			writeLock.lock();
			final int index = ids.indexOf(id);
			if (index > -1) {
				tasks.remove(index);
				patterns.remove(index);
				ids.remove(index);
				size--;
			}
		} finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * 获得指定位置的{@link Task}
	 * @param index 位置
	 * @return {@link Task}
	 * @since 3.1.1
	 */
	public Task getTask(int index) {
		final Lock readLock = lock.readLock();
		try {
			readLock.lock();
			return tasks.get(index);
		} finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 获得指定id的{@link Task}
	 * @param id ID
	 * @return {@link Task}
	 * @since 3.1.1
	 */
	public Task getTask(String id) {
		final int index = ids.indexOf(id);
		if(index > -1) {
			return getTask(index);
		}
		return null;
	}
	
	/**
	 * 获得指定位置的{@link CronPattern}
	 * @param index 位置
	 * @return {@link CronPattern}
	 * @since 3.1.1
	 */
	public CronPattern getPattern(int index) {
		final Lock readLock = lock.readLock();
		try {
			readLock.lock();
			return patterns.get(index);
		} finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 获得指定id的{@link CronPattern}
	 * @param id ID
	 * @return {@link CronPattern}
	 * @since 3.1.1
	 */
	public CronPattern getPattern(String id) {
		final int index = ids.indexOf(id);
		if(index > -1) {
			return getPattern(index);
		}
		return null;
	}
	
	/**
	 * 如果时间匹配则执行相应的Task，带读锁
	 * @param millis 时间毫秒
	 * @param isMatchSecond 是否匹配秒
	 * @param isMatchYear 是否匹配年
	 */
	public void executeTaskIfMatch(long millis, boolean isMatchSecond, boolean isMatchYear){
		final Lock readLock = lock.readLock();
		try {
			readLock.lock();
			executeTaskIfMatchInternal(millis, isMatchSecond, isMatchYear);
		} finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 如果时间匹配则执行相应的Task，带读锁
	 * @param millis 时间毫秒
	 * @since 3.1.1
	 */
	public void executeTaskIfMatch(long millis){
		executeTaskIfMatch(millis, scheduler.matchSecond, scheduler.matchYear);
	}
	
	/**
	 * 如果时间匹配则执行相应的Task
	 * @param millis 时间毫秒
	 * @param isMatchSecond 是否匹配秒
	 * @param isMatchYear 是否匹配年
	 * @since 3.1.1
	 */
	protected void executeTaskIfMatchInternal(long millis, boolean isMatchSecond, boolean isMatchYear){
		for(int i = 0; i < size; i++){
			if(patterns.get(i).match(timezone, millis, isMatchSecond, isMatchYear)){
				this.scheduler.taskExecutorManager.spawnExecutor(tasks.get(i));
			}
		}
	}
	
	
	/**
	 * 如果时间匹配则执行相应的Task
	 * @param millis 时间毫秒
	 */
	protected void executeTaskIfMatchInternal(long millis){
		executeTaskIfMatchInternal(millis, scheduler.matchSecond, scheduler.matchYear);
	}
}
