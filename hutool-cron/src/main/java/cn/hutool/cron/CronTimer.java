package cn.hutool.cron;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * 定时任务计时器<br>
 * 计时器线程每隔一分钟检查一次任务列表，一旦匹配到执行对应的Task
 * @author Looly
 *
 */
public class CronTimer extends Thread{
	private static final Log log = LogFactory.get();
	
	/** 定时单元：秒 */
	private long TIMER_UNIT_SECOND = DateUnit.SECOND.getMillis();
	/** 定时单元：分 */
	private long TIMER_UNIT_MINUTE = DateUnit.MINUTE.getMillis();
	
	/** 定时任务是否已经被强制关闭 */
	private boolean isStoped;
	private Scheduler scheduler;
	
	/**
	 * 构造
	 * @param scheduler {@link Scheduler}
	 */
	public CronTimer(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	@Override
	public void run() {
		final long timerUnit = this.scheduler.matchSecond ? TIMER_UNIT_SECOND : TIMER_UNIT_MINUTE;
		
		long thisTime = System.currentTimeMillis();
		long nextTime;
		long sleep;
		while(false == isStoped){
			//下一时间计算是按照上一个执行点开始时间计算的
			nextTime = ((thisTime / timerUnit) + 1) * timerUnit;
			sleep = nextTime - System.currentTimeMillis();
			if (sleep > 0 && false == ThreadUtil.safeSleep(sleep)) {
				//等待直到下一个时间点，如果被中断直接退出Timer
				break;
			}
			
			//执行点，时间记录为执行开始的时间，而非结束时间
			thisTime = System.currentTimeMillis();
			spawnLauncher(thisTime);
		}
		log.debug("Hutool Cron Timer stoped.");
	}
	
	/**
	 * 关闭定时器
	 */
	synchronized public void stopTimer() {
		this.isStoped = true;
		ThreadUtil.interupt(this, true);
	}
	
	/**
	 * 启动匹配
	 * @param millis 当前时间
	 */
	private void spawnLauncher(final long millis){
		this.scheduler.taskLauncherManager.spawnLauncher(millis);
	}
}
