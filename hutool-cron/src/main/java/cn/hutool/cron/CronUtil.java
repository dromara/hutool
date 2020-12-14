
package cn.hutool.cron;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.task.Task;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingRuntimeException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 定时任务工具类<br>
 * 此工具持有一个全局{@link Scheduler}，所有定时任务在同一个调度器中执行<br>
 * {@link #setMatchSecond(boolean)} 方法用于定义是否使用秒匹配模式，如果为true，则定时任务表达式中的第一位为秒，否则为分，默认是分
 * 
 * @author xiaoleilu
 *
 */
public class CronUtil {

	/** Crontab配置文件 */
	public static final String CRONTAB_CONFIG_PATH = "config/cron.setting";
	public static final String CRONTAB_CONFIG_PATH2 = "cron.setting";

	private static final Lock lock = new ReentrantLock();
	private static final Scheduler scheduler = new Scheduler();
	private static Setting crontabSetting;

	/**
	 * 自定义定时任务配置文件
	 * 
	 * @param cronSetting 定时任务配置文件
	 */
	public static void setCronSetting(Setting cronSetting) {
		crontabSetting = cronSetting;
	}

	/**
	 * 自定义定时任务配置文件路径
	 * 
	 * @param cronSettingPath 定时任务配置文件路径（相对绝对都可）
	 */
	public static void setCronSetting(String cronSettingPath) {
		try {
			crontabSetting = new Setting(cronSettingPath, Setting.DEFAULT_CHARSET, false);
		} catch (SettingRuntimeException | NoResourceException e) {
			// ignore setting file parse error and no config error
		}
	}

	/**
	 * 设置是否支持秒匹配<br>
	 * 此方法用于定义是否使用秒匹配模式，如果为true，则定时任务表达式中的第一位为秒，否则为分，默认是分<br>
	 * 
	 * @param isMatchSecond <code>true</code>支持，<code>false</code>不支持
	 */
	public static void setMatchSecond(boolean isMatchSecond) {
		scheduler.setMatchSecond(isMatchSecond);
	}

	/**
	 * 加入定时任务
	 * 
	 * @param schedulingPattern 定时任务执行时间的crontab表达式
	 * @param task 任务
	 * @return 定时任务ID
	 */
	public static String schedule(String schedulingPattern, Task task) {
		return scheduler.schedule(schedulingPattern, task);
	}

	/**
	 * 加入定时任务
	 * 
	 * @param id 定时任务ID
	 * @param schedulingPattern 定时任务执行时间的crontab表达式
	 * @param task 任务
	 * @return 定时任务ID
	 * @since 3.3.0
	 */
	public static String schedule(String id, String schedulingPattern, Task task) {
		scheduler.schedule(id, schedulingPattern, task);
		return id;
	}

	/**
	 * 加入定时任务
	 * 
	 * @param schedulingPattern 定时任务执行时间的crontab表达式
	 * @param task 任务
	 * @return 定时任务ID
	 */
	public static String schedule(String schedulingPattern, Runnable task) {
		return scheduler.schedule(schedulingPattern, task);
	}

	/**
	 * 批量加入配置文件中的定时任务
	 * 
	 * @param cronSetting 定时任务设置文件
	 */
	public static void schedule(Setting cronSetting) {
		scheduler.schedule(cronSetting);
	}

	/**
	 * 移除任务
	 * 
	 * @param schedulerId 任务ID
	 */
	public static void remove(String schedulerId) {
		scheduler.deschedule(schedulerId);
	}

	/**
	 * 更新Task的执行时间规则
	 * 
	 * @param id Task的ID
	 * @param pattern {@link CronPattern}
	 * @since 4.0.10
	 */
	public static void updatePattern(String id, CronPattern pattern) {
		scheduler.updatePattern(id, pattern);
	}

	/**
	 * @return 获得Scheduler对象
	 */
	public static Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * 开始，非守护线程模式
	 * 
	 * @see #start(boolean)
	 */
	public static void start() {
		start(false);
	}

	/**
	 * 开始
	 * 
	 * @param isDaemon 是否以守护线程方式启动，如果为true，则在调用{@link #stop()}方法后执行的定时任务立即结束，否则等待执行完毕才结束。
	 */
	synchronized public static void start(boolean isDaemon) {
		if (scheduler.isStarted()) {
			throw new UtilException("Scheduler has been started, please stop it first!");
		}
		
		lock.lock();
		try {
			if (null == crontabSetting) {
				// 尝试查找config/cron.setting
				setCronSetting(CRONTAB_CONFIG_PATH);
			}
			// 尝试查找cron.setting
			if (null == crontabSetting) {
				setCronSetting(CRONTAB_CONFIG_PATH2);
			}
		} finally {
			lock.unlock();
		}

		schedule(crontabSetting);
		scheduler.start(isDaemon);
	}

	/**
	 * 重新启动定时任务<br>
	 * 此方法会清除动态加载的任务，重新启动后，守护线程与否与之前保持一致
	 */
	public static void restart() {
		lock.lock();
		try {
			if (null != crontabSetting) {
				//重新读取配置文件
				crontabSetting.load();
			}
			if (scheduler.isStarted()) {
				//关闭并清除已有任务
				stop();
			}
		} finally {
			lock.unlock();
		}
		
		//重新加载任务
		schedule(crontabSetting);
		//重新启动
		scheduler.start();
	}

	/**
	 * 停止
	 */
	public static void stop() {
		scheduler.stop(true);
	}

}
