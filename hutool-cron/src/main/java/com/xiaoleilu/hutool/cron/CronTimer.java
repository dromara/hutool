package com.xiaoleilu.hutool.cron;

import com.xiaoleilu.hutool.util.ThreadUtil;

/**
 * 定时任务计时器<br>
 * 计时器线程每隔一分钟检查一次任务列表，一旦匹配到执行对应的Task
 * @author Looly
 *
 */
public class CronTimer extends Thread{
	
	private Scheduler scheduler;
	
	public CronTimer(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	@Override
	public void run() {
		long thisTime = System.currentTimeMillis();
		long nextMinute;
		long sleep;
		while(true){
			//下一分钟计算是按照上一个执行点开始时间计算的
			nextMinute = ((thisTime / 60000) + 1) * 60000;//使用整分钟数做为一个判断点
			sleep = nextMinute - System.currentTimeMillis();
			if (sleep > 0) {
				boolean isContinue = ThreadUtil.safeSleep(sleep);
				if(false == isContinue){
					break;
				}
			}
			
			//执行点，时间记录为执行开始的时间，而非结束时间
			thisTime = System.currentTimeMillis();
			spawnLauncher(thisTime);
		}
	}
	
	/**
	 * 启动匹配
	 * @param millis 当前时间
	 */
	private void spawnLauncher(final long millis){
		ThreadUtil.execute(new Runnable(){
			
			@Override
			public void run() {
				scheduler.tasks.executeTaskIfMatch(scheduler.getTimeZone(), millis);
			}
		});
	}
}
