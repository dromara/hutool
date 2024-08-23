package org.dromara.hutool.core.thread.ratelimiter;

import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class SemaphoreRateLimiterTest {

	@Test
	void test() {
		final RateLimiterConfig rateLimiterConfig = RateLimiterConfig.of()
			.setCapacity(5)
			.setMaxReleaseCount(5)
			.setRefreshPeriod(Duration.ofMillis(300))
			.setTimeout(Duration.ofSeconds(5));
		final TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(rateLimiterConfig);

		final boolean b = rateLimiter.tryAcquire(5);
		Assertions.assertTrue(b);
		// 超过数量
		final boolean b1 = rateLimiter.tryAcquire(1);
		Assertions.assertFalse(b1);

		ThreadUtil.sleep(400);

		// 填充新的许可
		final boolean b2 = rateLimiter.tryAcquire(5);
		Assertions.assertTrue(b2);
		// 超过数量
		final boolean b3 = rateLimiter.tryAcquire(1);
		Assertions.assertFalse(b3);

		rateLimiter.close();
	}
}
