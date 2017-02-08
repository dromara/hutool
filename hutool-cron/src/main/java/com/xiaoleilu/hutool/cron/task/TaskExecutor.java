package com.xiaoleilu.hutool.cron.task;

/**
 * 任务执行器
 * @author Looly
 *
 */
public class TaskExecutor extends Thread{
	
	Task task;
	
	public TaskExecutor(Task task) {
		this.task = task;
	}
	
	@Override
	public void run() {
		task.execute();
	}
}
