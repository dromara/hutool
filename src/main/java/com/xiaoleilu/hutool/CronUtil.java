package com.xiaoleilu.hutool;

import it.sauronsoftware.cron4j.Scheduler;

import java.util.Map.Entry;

import org.slf4j.Logger;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 定时任务工具类
 * @author xiaoleilu
 *
 */
public class CronUtil {
	private static Logger log = Log.get();
	
	/** Crontab配置文件 */
	public final static String CRONTAB_CONFIG_PATH = "config/cron4j.setting";
	
	private final static Scheduler scheduler = new Scheduler();
	private static Setting crontab;
	
	/**
	 * 自定义定时任务配置文件
	 * @param cronSetting 定时任务配置文件 
	 */
	public static void setCronSetting(Setting cronSetting) {
		crontab = cronSetting;
	}
	
	/**
	 * 自定义定时任务配置文件路径
	 * @param cronSettingPath 定时任务配置文件路径（相对绝对都可）
	 */
	public static void setCronSetting(String cronSettingPath) {
		crontab = new Setting(cronSettingPath, Setting.DEFAULT_CHARSET, false);
	}
	
	/**
	 * 加入定时任务
	 * @param schedulingPattern 定时任务执行时间的crontab表达式
	 * @param task 任务
	 */
	public static void schedule(String schedulingPattern, Runnable task) {
		scheduler.schedule(schedulingPattern, task);
	}
	
	/**
	 * 批量加入配置文件中的定时任务
	 * @param cronSetting 定时任务设置文件
	 */
	public static void schedule(Setting cronSetting) {
		for (Entry<String, String> entry : cronSetting.entrySet()) {
			final String jobClass = entry.getKey();
			final String pattern = entry.getValue();
			try {
				final Runnable job = ClassUtil.newInstance(jobClass);
				schedule(pattern, job);
				log.info("Schedule [{} {}] added.", pattern, jobClass);
			} catch (Exception e) {
				Log.error(log, e, "Schedule [%s %s] add error!", pattern, jobClass);
			}
		}
	}
	
	/**
	 * 开始
	 */
	synchronized public static void start() {
		if(null == crontab) {
			setCronSetting(CRONTAB_CONFIG_PATH);
		}
		if(scheduler.isStarted()) {
			throw new UtilException("Scheduler has been started, please stop it first!");
		}
		
		schedule(crontab);
		scheduler.start();
	}
	
	/**
	 * 停止
	 */
	synchronized public static void stop() {
		scheduler.stop();
	}
	
}

