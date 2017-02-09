package com.xiaoleilu.hutool.cron.demo;

import com.xiaoleilu.hutool.cron.CronUtil;
import com.xiaoleilu.hutool.cron.task.Task;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.dialect.console.ConsoleLogFactory;

public class CronDemo {
	public static void main(String[] args) {
		LogFactory.setCurrentLogFactory(ConsoleLogFactory.class);
		
		CronUtil.setUseSecond(true);
		CronUtil.schedule("*/1 * * * *", new Task(){
			
			@Override
			public void execute() {
				Console.log("Task excuted.");
			}
		});
		
		CronUtil.start();
//		CronUtil.stop();
	}
}
