package cn.hutool.core.util;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * {@link IdUtil} 单元测试
 * 
 * @author looly
 *
 */
public class IdUtilTest {

	@Test
	public void randomUUIDTest() {
		String simpleUUID = IdUtil.simpleUUID();
		Assert.assertEquals(32, simpleUUID.length());

		String randomUUID = IdUtil.randomUUID();
		Assert.assertEquals(36, randomUUID.length());
	}
	
	@Test
	public void fastUUIDTest() {
		String simpleUUID = IdUtil.fastSimpleUUID();
		Assert.assertEquals(32, simpleUUID.length());
		
		String randomUUID = IdUtil.fastUUID();
		Assert.assertEquals(36, randomUUID.length());
	}

	/**
	 * UUID的性能测试
	 */
	@Test
	@Ignore
	public void benchTest() {
		TimeInterval timer = DateUtil.timer();
		for (int i = 0; i < 1000000; i++) {
			IdUtil.simpleUUID();
		}
		Console.log(timer.interval());

		timer.restart();
		for (int i = 0; i < 1000000; i++) {
			//noinspection ResultOfMethodCallIgnored
			UUID.randomUUID().toString().replace("-", "");
		}
		Console.log(timer.interval());
	}
	
	@Test
	public void objectIdTest() {
		String id = IdUtil.objectId();
		Assert.assertEquals(24, id.length());
	}
	
	@Test
	public void createSnowflakeTest() {
		Snowflake snowflake = IdUtil.createSnowflake(1, 1);
		long id = snowflake.nextId();
		Assert.assertTrue(id > 0);
	}
	
	@Test
	public void snowflakeBenchTest() {
		final Set<Long> set = new ConcurrentHashSet<>();
		final Snowflake snowflake = IdUtil.createSnowflake(1, 1);
		
		//线程数
		int threadCount = 100;
		//每个线程生成的ID数
		final int idCountPerThread = 10000;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for(int i =0; i < threadCount; i++) {
			ThreadUtil.execute(() -> {
				for(int i1 = 0; i1 < idCountPerThread; i1++) {
					long id = snowflake.nextId();
					set.add(id);
//						Console.log("Add new id: {}", id);
				}
				latch.countDown();
			});
		}
		
		//等待全部线程结束
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new UtilException(e);
		}
		Assert.assertEquals(threadCount * idCountPerThread, set.size());
	}
	
	@Test
	public void snowflakeBenchTest2() {
		final Set<Long> set = new ConcurrentHashSet<>();
		
		//线程数
		int threadCount = 100;
		//每个线程生成的ID数
		final int idCountPerThread = 10000;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for(int i =0; i < threadCount; i++) {
			ThreadUtil.execute(() -> {
				for(int i1 = 0; i1 < idCountPerThread; i1++) {
					long id = IdUtil.getSnowflake(1, 1).nextId();
					set.add(id);
//						Console.log("Add new id: {}", id);
				}
				latch.countDown();
			});
		}
		
		//等待全部线程结束
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new UtilException(e);
		}
		Assert.assertEquals(threadCount * idCountPerThread, set.size());
	}
}
