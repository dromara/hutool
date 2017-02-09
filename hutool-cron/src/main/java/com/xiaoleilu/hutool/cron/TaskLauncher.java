package com.xiaoleilu.hutool.cron;

/**
 * 作业启动器<br>
 * 负责检查<strong>TaskTable</strong>是否有匹配到此时运行的Task
 * 
 * @author Looly
 *
 */
public class TaskLauncher extends Thread{
	
	private Scheduler scheduler;
	private long millis;
	
	public TaskLauncher(Scheduler scheduler, long millis) {
		this.scheduler = scheduler;
		this.millis = millis;
	}
	
	@Override
	public void run() {
		scheduler.tasks.executeTaskIfMatch(millis);
	}
}
