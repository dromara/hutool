package cn.hutool.core.thread;

import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

public class ThreadUtilTest {

	@Test
	public void executeTest() {
		final boolean isValid = true;

		ThreadUtil.execute(() -> Assert.assertTrue(isValid));
	}

	@Test
	public void safeSleepTest() {
		final long sleepMillis = RandomUtil.randomLong(1, 1000);
		// 随机sleep时长，确保sleep时间足够
		final long l = System.currentTimeMillis();
		ThreadUtil.safeSleep(sleepMillis);
		Assert.assertTrue(System.currentTimeMillis() - l >= sleepMillis);
	}
}
