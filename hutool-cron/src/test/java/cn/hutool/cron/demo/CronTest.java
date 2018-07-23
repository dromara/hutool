package cn.hutool.cron.demo;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.InvokeTask;
import cn.hutool.cron.task.Task;

/**
 * 定时任务样例
 */
public class CronTest {

	@Test
	@Ignore
	public void customCronTest() {
		CronUtil.schedule("*/2 * * * * *", new Task() {

			@Override
			public void execute() {
				Console.log("Task excuted.");
			}
		});

		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.start();
	}

	@Test
	@Ignore
	public void cronTest() {
		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.getScheduler().setDaemon(false);
		CronUtil.start();

		ThreadUtil.sleep(3000);
		CronUtil.stop();
	}

	@Test
	@Ignore
	public void addAndRemoveTest() {
		String id = CronUtil.schedule("*/2 * * * * *", new Runnable() {

			@Override
			public void run() {
				Console.log("task running : 2s");
			}
		});

		Console.log(id);
		CronUtil.remove(id);

		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.start();
	}

	public static void main(String[] args) {
		// 测试守护线程是否对作业线程有效
		CronUtil.schedule("*/2 * * * * *", new InvokeTask("cn.hutool.cron.demo.TestJob.doWhileTest"));
		//当为守护线程时，stop方法调用后doWhileTest里的循环输出将终止，表示作业线程正常结束
		//当非守护线程时，stop方法调用后，不再产生新的作业，原作业正常执行。
		CronUtil.setMatchSecond(true);
		CronUtil.start(true);

		ThreadUtil.sleep(3000);
		CronUtil.stop();
	}
}
