package cn.hutool.core.lang;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.thread.ConcurrencyTester;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
	
	@Test
	public void snowflakeGetTest(){
		//构建Snowflake，提供终端ID和数据中心ID
		Snowflake idWorker = new Snowflake(1, 2);
		long nextId = idWorker.nextId();
		
		Assert.assertEquals(1, idWorker.getWorkerId(nextId));
		Assert.assertEquals(2, idWorker.getDataCenterId(nextId));
		Assert.assertTrue(idWorker.getGenerateDateTime(nextId) - System.currentTimeMillis() < 10);
	}

	@Test
	@Ignore
	public void uniqueTest(){
		// 测试并发环境下生成ID是否重复
		Snowflake snowflake = IdUtil.createSnowflake(0, 0);

		Set<Long> ids = new ConcurrentHashSet<>();
		ConcurrencyTester tester = ThreadUtil.concurrencyTest(100, () -> {
			for (int i = 0; i < 5000; i++) {
				if(false == ids.add(snowflake.nextId())){
					throw new UtilException("重复ID！");
				}
			}
		});
	}
}
