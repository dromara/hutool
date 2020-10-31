package cn.hutool.cron;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.Serializable;

/**
 * 定时任务计时器<br>
 * 计时器线程每隔一分钟（一秒钟）检查一次任务列表，一旦匹配到执行对应的Task
 * @author Looly
 *
 */
public class CronTimer extends Thread implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.get();

	/** 定时单元：秒 */
	private final long TIMER_UNIT_SECOND = DateUnit.SECOND.getMillis();
	/** 定时单元：分 */
	private final long TIMER_UNIT_MINUTE = DateUnit.MINUTE.getMillis();
	
	/** 定时任务是否已经被强制关闭 */
	private boolean isStop;
	private final Scheduler scheduler;
	
	/**
	 * 构造
	 * @param scheduler {@link Scheduler}
	 */
	public CronTimer(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	@Override
	public void run() {
		final long timerUnit = this.scheduler.config.matchSecond ? TIMER_UNIT_SECOND : TIMER_UNIT_MINUTE;
		
		long thisTime = System.currentTimeMillis();
		long nextTime;
		long sleep;
		while(false == isStop){
			//下一时间计算是按照上一个执行点开始时间计算的
			//此处除以定时单位是为了清零单位以下部分，例如单位是分则秒和毫秒清零
			nextTime = ((thisTime / timerUnit) + 1) * timerUnit;
			sleep = nextTime - System.currentTimeMillis();
			if(isValidSleepMillis(sleep, timerUnit)){
				if (false == ThreadUtil.safeSleep(sleep)) {
					//等待直到下一个时间点，如果被中断直接退出Timer
					break;
				}
				//执行点，时间记录为执行开始的时间，而非结束时间
				thisTime = System.currentTimeMillis();
				spawnLauncher(thisTime);
			}
		}
		log.debug("Hutool-cron timer stopped.");
	}
	
	/**
	 * 关闭定时器
	 */
	synchronized public void stopTimer() {
		this.isStop = true;
		ThreadUtil.interrupt(this, true);
	}
	
	/**
	 * 启动匹配
	 * @param millis 当前时间
	 */
	private void spawnLauncher(final long millis){
		this.scheduler.taskLauncherManager.spawnLauncher(millis);
	}

	/**
	 * 检查是否为有效的sleep毫秒数，包括：
	 * <pre>
	 *     1. 是否&gt;0，防止用户向未来调整时间
	 *     1. 是否&lt;两倍的间隔单位，防止用户向历史调整时间
	 * </pre>
	 *
	 * @param millis 毫秒数
	 * @param timerUnit 定时单位，为秒或者分的毫秒值
	 * @return 是否为有效的sleep毫秒数
	 * @since 5.3.2
	 */
	private static boolean isValidSleepMillis(long millis, long timerUnit){
		return millis > 0 &&
				// 防止用户向前调整时间导致的长时间sleep
				millis < (2 * timerUnit);
	}
}
