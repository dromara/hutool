package cn.hutool.cron.demo;

import cn.hutool.core.lang.Console;
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
		
		String id = CronUtil.schedule("*/2 * * * * *", new Runnable() {
			
			@Override
			public void run() {
				Console.log("task running : 2s");
			}
		});
		
		Console.log(id);
		CronUtil.remove(id);
		
//		CronUtil.stop();
	}
}
