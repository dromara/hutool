package com.xiaoleilu.hutool.cron;

import com.xiaoleilu.hutool.cron.task.Task;
import com.xiaoleilu.hutool.cron.task.TaskTable;

/**
 * 任务调度器
 * @author Looly
 *
 */
public class Scheduler {
	private TaskTable tasks = new TaskTable();
	private CronTimer timer = new CronTimer();
	
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
	public synchronized void remove(String id) throws IndexOutOfBoundsException {
		this.tasks.remove(id);
	}
	
	public void start(){
		timer.start();
	}
	
	public void stop(){
	}
}
