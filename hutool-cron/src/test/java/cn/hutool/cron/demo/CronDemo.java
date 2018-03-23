package cn.hutool.cron.demo;

import cn.hutool.cron.CronUtil;

/**
 * 定时任务样例
 */
public class CronDemo {
	public static void main(String[] args) {
//		CronUtil.schedule("*/2 * * * * *", new Task(){
//			
//			@Override
//			public void execute() {
//				Console.log("Task excuted.");
//			}
//		});
		
		//支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		
		CronUtil.start();
//		CronUtil.stop();
	}
}
