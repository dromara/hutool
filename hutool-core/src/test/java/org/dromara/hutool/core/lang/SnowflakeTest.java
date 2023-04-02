package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.collection.ConcurrentHashSet;
import org.dromara.hutool.core.exceptions.UtilException;
import org.dromara.hutool.core.lang.id.IdUtil;
import org.dromara.hutool.core.lang.id.Snowflake;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Snowflake单元测试
 * @author Looly
 *
 */
public class SnowflakeTest {

	@Test
	public void snowflakeTest1(){
		//构建Snowflake，提供终端ID和数据中心ID
		final Snowflake idWorker = new Snowflake(0, 0);
		final long nextId = idWorker.nextId();
		Assertions.assertTrue(nextId > 0);
	}

	@Test
	public void snowflakeTest(){
		final HashSet<Long> hashSet = new HashSet<>();

		//构建Snowflake，提供终端ID和数据中心ID
		final Snowflake idWorker = new Snowflake(0, 0);
		for (int i = 0; i < 1000; i++) {
			final long id = idWorker.nextId();
			hashSet.add(id);
		}
		Assertions.assertEquals(1000L, hashSet.size());
	}

	@Test
	public void snowflakeGetTest(){
		//构建Snowflake，提供终端ID和数据中心ID
		final Snowflake idWorker = new Snowflake(1, 2);
		final long nextId = idWorker.nextId();

		Assertions.assertEquals(1, idWorker.getWorkerId(nextId));
		Assertions.assertEquals(2, idWorker.getDataCenterId(nextId));
		Assertions.assertTrue(idWorker.getGenerateDateTime(nextId) - System.currentTimeMillis() < 10);
	}

	@Test
	@Disabled
	public void uniqueTest(){
		// 测试并发环境下生成ID是否重复
		final Snowflake snowflake = IdUtil.getSnowflake(0, 0);

		final Set<Long> ids = new ConcurrentHashSet<>();
		ThreadUtil.concurrencyTest(100, () -> {
			for (int i = 0; i < 50000; i++) {
				if(false == ids.add(snowflake.nextId())){
					throw new UtilException("重复ID！");
				}
			}
		});
	}

	@Test
	public void getSnowflakeLengthTest(){
		for (int i = 0; i < 1000; i++) {
			final long l = IdUtil.getSnowflake(0, 0).nextId();
			Assertions.assertEquals(19, StrUtil.toString(l).length());
		}
	}

	@Test
	@Disabled
	public void snowflakeRandomSequenceTest(){
		final Snowflake snowflake = new Snowflake(null, 0, 0,
				false, Snowflake.DEFAULT_TIME_OFFSET, 2);
		for (int i = 0; i < 1000; i++) {
			final long id = snowflake.nextId();
			Console.log(id);
			ThreadUtil.sleep(10);
		}
	}

	@Test
	@Disabled
	public void uniqueOfRandomSequenceTest(){
		// 测试并发环境下生成ID是否重复
		final Snowflake snowflake = new Snowflake(null, 0, 0,
				false, Snowflake.DEFAULT_TIME_OFFSET, 100);

		final Set<Long> ids = new ConcurrentHashSet<>();
		ThreadUtil.concurrencyTest(100, () -> {
			for (int i = 0; i < 50000; i++) {
				if(false == ids.add(snowflake.nextId())){
					throw new UtilException("重复ID！");
				}
			}
		});
	}
}
