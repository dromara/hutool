package com.xiaoleilu.hutool.cron.demo;

import com.xiaoleilu.hutool.cron.CronUtil;
import com.xiaoleilu.hutool.cron.task.Task;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.dialect.console.ConsoleLogFactory;

/**
 * 定时任务样例
 */
public class CronDemo {
	public static void main(String[] args) {
		LogFactory.setCurrentLogFactory(ConsoleLogFactory.class);
		
		CronUtil.schedule("*/2 * * * * *", new Task(){
			
			@Override
			public void execute() {
				Console.log("Task excuted.");
			}
		});
		
		CronUtil.setMatchSecond(true);
		
		CronUtil.start();
//		CronUtil.stop();
	}
}
