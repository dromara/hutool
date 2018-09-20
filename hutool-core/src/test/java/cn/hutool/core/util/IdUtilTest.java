package cn.hutool.core.util;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Snowflake;

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
}
