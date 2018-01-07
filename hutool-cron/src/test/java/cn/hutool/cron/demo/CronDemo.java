package cn.hutool.cron.demo;

import cn.hutool.cron.CronUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.log.dialect.console.ConsoleLogFactory;

/**
 * 定时任务样例
 */
public class CronDemo {
	public static void main(String[] args) {
		LogFactory.setCurrentLogFactory(ConsoleLogFactory.class);
		
//		CronUtil.schedule("*/2 * * * * *", new Task(){
//			
//			@Override
//			public void execute() {
//				Console.log("Task excuted.");
//			}
//		});
		
		//支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		//支持年匹配
		CronUtil.setMatchYear(true);
		
		CronUtil.start();
//		CronUtil.stop();
	}
}
