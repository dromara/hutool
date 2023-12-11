package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author: liufuqiang
 * @date: 2023-12-11 17:04
 */
public class CodeWatchUtilTest {

	@Test
	public void printDefault() throws InterruptedException {
		CodeWatchUtil.init("任务id");

		// 任务1
		CodeWatchUtil.start("任务一");
		Thread.sleep(1000);

		// 任务2
		CodeWatchUtil.start("任务二");
		Thread.sleep(2000);
		Console.log(CodeWatchUtil.printCurrentTask());


		// 打印出耗时
		Console.log(CodeWatchUtil.prettyPrint());
	}

	@Test
	public void printUnit() throws InterruptedException {
		CodeWatchUtil.init("任务id");
		method01();
		method02();
		Console.log(CodeWatchUtil.prettyPrint(TimeUnit.MILLISECONDS));
	}

	private static void method01() throws InterruptedException {
		CodeWatchUtil.start("方法一");
		Thread.sleep(1000);
	}

	private static void method02() throws InterruptedException {
		CodeWatchUtil.start("方法二");
		Thread.sleep(2000);
		CodeWatchUtil.stop();
		Console.log(CodeWatchUtil.printCurrentTask(TimeUnit.MILLISECONDS));
	}
}
