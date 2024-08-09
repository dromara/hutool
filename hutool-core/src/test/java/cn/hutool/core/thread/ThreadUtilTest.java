package cn.hutool.core.thread;

import cn.hutool.core.util.RandomUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadUtilTest {


	@Test
	public void newExecutorTest(){
		ThreadPoolExecutor executor = ThreadUtil.newExecutor(5);
		// 查询线程池 线程数
		assertEquals(5, executor.getCorePoolSize());
	}

	@Test
	public void executeTest() {
		final boolean isValid = true;

		ThreadUtil.execute(() -> assertTrue(isValid));
	}

	@Test
	public void safeSleepTest() {
		final long sleepMillis = RandomUtil.randomLong(1, 1000);
		// 随机sleep时长，确保sleep时间足够
		final long l = System.currentTimeMillis();
		ThreadUtil.safeSleep(sleepMillis);
		assertTrue(System.currentTimeMillis() - l >= sleepMillis);
	}
}
