package org.dromara.hutool.date;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

	/**
	 * https://gitee.com/dromara/hutool/issues/I6HSBG
	 */
	@Test
	public void prettyPrintTest() {
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start("任务1");
		ThreadUtil.sleep(1);
		stopWatch.stop();
		stopWatch.start("任务2");
		ThreadUtil.sleep(200);
		stopWatch.stop();

		Console.log(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
	}
}
