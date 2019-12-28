package cn.hutool.core.lang;

import cn.hutool.core.date.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
		HashSet<Long> hashSet;
		
		//构建Snowflake，提供终端ID和数据中心ID
		Snowflake idWorker = new Snowflake(0, 0);
		hashSet = IntStream.range(0, 1000).mapToLong(i -> idWorker.nextId()).boxed().collect(Collectors.toCollection(HashSet::new));
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
	public void bitwiseOperationTest(){
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("^");
		Integer count = Integer.MAX_VALUE;
		for(int i = 0; i < count; ++i){
			long j = -1L ^ (-1L << 5L);
		}
		stopWatch.stop();
		stopWatch.start("~");
		for(int i = 0; i < count; ++i){
			long j = ~(-1L << 5L);
		}
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
		Assert.assertEquals(-1L ^ (-1L << 5L),~(-1L << 5L));
	}
}
