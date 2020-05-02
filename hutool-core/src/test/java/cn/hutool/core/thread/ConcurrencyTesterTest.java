package cn.hutool.core.thread;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;

public class ConcurrencyTesterTest {

	@Test
	@Ignore
	public void concurrencyTesterTest() {
		ConcurrencyTester tester = ThreadUtil.concurrencyTest(100, () -> {
			long delay = RandomUtil.randomLong(100, 1000);
			ThreadUtil.sleep(delay);
			Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
		});
		Console.log(tester.getInterval());
	}
}
