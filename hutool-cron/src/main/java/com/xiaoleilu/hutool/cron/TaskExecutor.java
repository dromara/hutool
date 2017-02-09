package com.xiaoleilu.hutool.cron;

import com.xiaoleilu.hutool.cron.task.Task;

/**
 * 作业执行器<br>
 * 执行具体的作业，执行完毕销毁
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
