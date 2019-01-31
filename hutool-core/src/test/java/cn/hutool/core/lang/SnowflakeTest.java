package cn.hutool.core.lang;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Snowflake单元测试
 * @author Looly
 *
 */
public class SnowflakeTest {
	
	@Test
	public void snowflakeTest1(){
		//构建Snowflake，提供终端ID和数据中心ID
		Snowflake idWorker = new Snowflake(0, 0);
		long nextId = idWorker.nextId();
		Assert.assertTrue(nextId > 0);
	}
	
	@Test
	public void snowflakeTest(){
		HashSet<Long> hashSet = new HashSet<>();
		
		//构建Snowflake，提供终端ID和数据中心ID
		Snowflake idWorker = new Snowflake(0, 0);
		for (int i = 0; i < 1000; i++) {
			long id = idWorker.nextId();
			hashSet.add(id);
		}
		Assert.assertEquals(1000L, hashSet.size());
	}
}
