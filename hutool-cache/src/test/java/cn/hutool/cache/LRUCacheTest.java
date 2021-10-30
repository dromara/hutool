package cn.hutool.cache;

import cn.hutool.cache.impl.LRUCache;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 见：https://github.com/dromara/hutool/issues/1895<br>
 * 并发问题测试，在5.7.15前，LRUCache存在并发问题，多线程get后，map结构变更，导致null的位置不确定，
 * 并可能引起死锁。
 */
public class LRUCacheTest {

	@Test
	public void readWriteTest() throws InterruptedException {
		LRUCache<Integer, Integer> cache = CacheUtil.newLRUCache(10);
		for (int i = 0; i < 10; i++) {
			cache.put(i, i);
		}

		CountDownLatch countDownLatch = new CountDownLatch(10);
		// 10个线程分别读0-9 10000次
		for (int i = 0; i < 10; i++) {
			int finalI = i;
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
		StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb1.append(cache.get(i));
		}
		Assert.assertEquals("0123456789", sb1.toString());

		// 新加11，此时0最久未使用，应该淘汰0
		cache.put(11, 11);

		StringBuilder sb2 = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			sb2.append(cache.get(i));
		}
		Assert.assertEquals("null123456789", sb2.toString());
	}
}
