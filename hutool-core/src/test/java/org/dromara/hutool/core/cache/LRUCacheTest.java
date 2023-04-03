package org.dromara.hutool.core.cache;

import org.dromara.hutool.core.cache.impl.LRUCache;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 见：<a href="https://github.com/dromara/hutool/issues/1895">https://github.com/dromara/hutool/issues/1895</a><br>
 * 并发问题测试，在5.7.15前，LRUCache存在并发问题，多线程get后，map结构变更，导致null的位置不确定，
 * 并可能引起死锁。
 */
public class LRUCacheTest {

	@Test
	@Disabled
	public void putTest(){
		//https://github.com/dromara/hutool/issues/2227
		final LRUCache<String, String> cache = CacheUtil.newLRUCache(100, 10);
		for (int i = 0; i < 10000; i++) {
			//ThreadUtil.execute(()-> cache.put(RandomUtil.randomString(5), "1243", 10));
			ThreadUtil.execute(()-> cache.get(RandomUtil.randomString(5), ()->RandomUtil.randomString(10)));
		}
		ThreadUtil.sleep(3000);
	}

	@Test
	public void readWriteTest() throws InterruptedException {
		final LRUCache<Integer, Integer> cache = CacheUtil.newLRUCache(10);
		for (int i = 0; i < 10; i++) {
			cache.put(i, i);
		}

		final CountDownLatch countDownLatch = new CountDownLatch(10);
		// 10个线程分别读0-9 10000次
		for (int i = 0; i < 10; i++) {
			final int finalI = i;
			new Thread(() -> {
				for (int j = 0; j < 10000; j++) {
					cache.get(finalI);
				}
				countDownLatch.countDown();
			}).start();
		}
		// 等待读线程结束
		countDownLatch.await();
		// 按顺序读0-9
		final StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb1.append(cache.get(i));
		}
		Assertions.assertEquals("0123456789", sb1.toString());

		// 新加11，此时0最久未使用，应该淘汰0
		cache.put(11, 11);

		final StringBuilder sb2 = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb2.append(cache.get(i));
		}
		Assertions.assertEquals("null123456789", sb2.toString());
	}

	@Test
	public void issue2647Test(){
		final AtomicInteger removeCount = new AtomicInteger();

		final LRUCache<String, Integer> cache = CacheUtil.newLRUCache(3,1);
		cache.setListener((key, value) -> {
			// 共移除7次
			removeCount.incrementAndGet();
			//Console.log("Start remove k-v, key:{}, value:{}", key, value);
		});

		for (int i = 0; i < 10; i++) {
			cache.put(StrUtil.format("key-{}", i), i);
		}

		Assertions.assertEquals(7, removeCount.get());
		Assertions.assertEquals(3, cache.size());
	}
}
