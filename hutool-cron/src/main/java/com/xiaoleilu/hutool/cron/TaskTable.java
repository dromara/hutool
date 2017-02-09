package com.xiaoleilu.hutool.cron;

import java.util.ArrayList;
import java.util.TimeZone;

import com.xiaoleilu.hutool.cron.task.Task;

/**
 * 定时任务表
 * @author Looly
 *
 */
public class TaskTable {
	
	private Scheduler scheduler;
	private TimeZone timezone;
	
	private ArrayList<String> ids = new ArrayList<>();
	private ArrayList<CronPattern> patterns = new ArrayList<>();
	private ArrayList<Task> tasks = new ArrayList<>();
	private int size;
	
	public TaskTable(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.timezone = scheduler.getTimeZone();
	}
	
	/**
	 * 新增Task
	 * @param id ID
	 * @param pattern {@link CronPattern}
	 * @param task {@link Task}
	 * @return this
	 */
	public TaskTable add(String id, CronPattern pattern, Task task){
		if(ids.contains(id)){
			throw new CronException("Id [{}] has been existed!", id);
		}
		ids.add(id);
		patterns.add(pattern);
		tasks.add(task);
		size++;
		return this;
	}
	
	/**
	 * 移除Task
	 * @param id Task的ID
	 */
	public synchronized void remove(String id) throws IndexOutOfBoundsException {
		int index = ids.indexOf(id);
		if (index > -1) {
			tasks.remove(index);
			patterns.remove(index);
			ids.remove(index);
		}
	}
	
	/**
	 * 如果时间匹配则执行相应的Task
	 * @param timezone 时区
	 * @param millis 时间毫秒
	 */
	public void executeTaskIfMatch(long millis){
		for(int i = 0; i < size; i++){
			if(patterns.get(i).match(timezone, millis)){
				this.scheduler.spawnExecutor(tasks.get(i));
			}
		}
	}
}
