package looly.github.hutool;

import it.sauronsoftware.cron4j.Scheduler;

import java.util.Map.Entry;

import org.slf4j.Logger;

/**
 * 定时任务工具类
 * @author xiaoleilu
 *
 */
public class CronUtil {
	private static Logger log = Log.get();
	
	/** Crontab配置文件 */
	public final static String CRONTAB_CONFIG_PATH = "config/cron4j.setting";
	
	private static Scheduler scheduler = new Scheduler();
	
	/**
	 * 加入定时任务
	 * @param schedulingPattern 定时任务执行时间的crontab表达式
	 * @param task 任务
	 */
	public static void schedule(String schedulingPattern, Runnable task) {
		scheduler.schedule(schedulingPattern, task);
	}
	
	/**
	 * 开始
	 */
	public static void start() {
		Setting crontab = new Setting(CRONTAB_CONFIG_PATH, Setting.DEFAULT_CHARSET, false);
		for (Entry<String, String> entry : crontab.entrySet()) {
			final String jobClass = entry.getKey();
			final String pattern = entry.getValue();
			try {
				final Class<?> clazz = Class.forName(jobClass);
				final Runnable job = (Runnable) clazz.newInstance();
				scheduler.schedule(pattern, job);
				log.info("Schedule [{} {}] added.", pattern, jobClass);
			} catch (Exception e) {
				Log.error(log, e, "Schedule [%s %s] add error!", pattern, jobClass);
			}
		}
		
		scheduler.start();
	}
	
	/**
	 * 停止
	 */
	public static void stop() {
		scheduler.stop();
	}
	
	public static void main(String[] args) {
		start();
	}
}

