/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.collection.ConcurrentHashSet;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.core.data.id.Snowflake;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
		final String simpleUUID = IdUtil.simpleUUID();
		Assertions.assertEquals(32, simpleUUID.length());

		final String randomUUID = IdUtil.randomUUID();
		Assertions.assertEquals(36, randomUUID.length());
	}

	@Test
	public void fastUUIDTest() {
		final String simpleUUID = IdUtil.fastSimpleUUID();
		Assertions.assertEquals(32, simpleUUID.length());

		final String randomUUID = IdUtil.fastUUID();
		Assertions.assertEquals(36, randomUUID.length());
	}

	/**
	 * UUID的性能测试
	 */
	@Test
	@Disabled
	public void benchTest() {
		final StopWatch timer = DateUtil.createStopWatch();
		timer.start();
		for (int i = 0; i < 1000000; i++) {
			IdUtil.simpleUUID();
		}
		timer.stop();
		Console.log(timer.getLastTaskTimeMillis());

		timer.start();
		for (int i = 0; i < 1000000; i++) {
			//noinspection ResultOfMethodCallIgnored
			UUID.randomUUID().toString().replace("-", "");
		}
		timer.stop();
		Console.log(timer.getLastTaskTimeMillis());
	}

	@Test
	public void objectIdTest() {
		final String id = IdUtil.objectId();
		Assertions.assertEquals(24, id.length());
	}

	@Test
	public void getSnowflakeTest() {
		final Snowflake snowflake = IdUtil.getSnowflake(1, 1);
		final long id = snowflake.next();
		Assertions.assertTrue(id > 0);
	}

	@Test
	@Disabled
	public void snowflakeBenchTest() {
		final Set<Long> set = new ConcurrentHashSet<>();
		final Snowflake snowflake = IdUtil.getSnowflake(1, 1);

		//线程数
		final int threadCount = 100;
		//每个线程生成的ID数
		final int idCountPerThread = 10000;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for(int i =0; i < threadCount; i++) {
			ThreadUtil.execute(() -> {
				for(int i1 = 0; i1 < idCountPerThread; i1++) {
					final long id = snowflake.next();
					set.add(id);
//						Console.log("Add new id: {}", id);
				}
				latch.countDown();
			});
		}

		//等待全部线程结束
		try {
			latch.await();
		} catch (final InterruptedException e) {
			throw new HutoolException(e);
		}
		Assertions.assertEquals(threadCount * idCountPerThread, set.size());
	}

	@Test
	@Disabled
	public void snowflakeBenchTest2() {
		final Set<Long> set = new ConcurrentHashSet<>();

		//线程数
		final int threadCount = 100;
		//每个线程生成的ID数
		final int idCountPerThread = 10000;
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for(int i =0; i < threadCount; i++) {
			ThreadUtil.execute(() -> {
				for(int i1 = 0; i1 < idCountPerThread; i1++) {
					final long id = IdUtil.getSnowflake(1, 1).next();
					set.add(id);
//						Console.log("Add new id: {}", id);
				}
				latch.countDown();
			});
		}

		//等待全部线程结束
		try {
			latch.await();
		} catch (final InterruptedException e) {
			throw new HutoolException(e);
		}
		Assertions.assertEquals(threadCount * idCountPerThread, set.size());
	}

	@Test
	public void getDataCenterIdTest(){
		//按照mac地址算法拼接的算法，maxDatacenterId应该是0xffffffffL>>6-1此处暂时按照0x7fffffffffffffffL-1，防止最后取模溢出
		final long dataCenterId = IdUtil.getDataCenterId(Long.MAX_VALUE);
		Assertions.assertTrue(dataCenterId >= 0);
	}
}
