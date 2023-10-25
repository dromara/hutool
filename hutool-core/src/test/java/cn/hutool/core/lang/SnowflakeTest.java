package cn.hutool.core.lang;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
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

	/**
	 * 测试-根据传入时间戳-计算ID起终点
	 */
	@Test
	public void snowflakeTestGetIdScope() {
		long workerId = RandomUtil.randomLong(31);
		long dataCenterId = RandomUtil.randomLong(31);
		Snowflake idWorker = new Snowflake(workerId, dataCenterId);
		long generatedId = idWorker.nextId();
		// 随机忽略数据中心和工作机器的占位
		boolean ignore = RandomUtil.randomBoolean();
		long createTimestamp = idWorker.getGenerateDateTime(generatedId);
		Pair<Long, Long> idScope = idWorker.getIdScopeByTimestamp(createTimestamp, createTimestamp, ignore);
		long startId = idScope.getKey();
		long endId = idScope.getValue();

		System.out.println(longToBinaryReadable(generatedId) + " = generatedId longToBinaryReadable");
		System.out.println(longToBinaryReadable(startId) + " = startId longToBinaryReadable");
		System.out.println(longToBinaryReadable(endId) + " = endId longToBinaryReadable");
		// 起点终点相差比较
		long trueOffSet = endId - startId;
		// 忽略数据中心和工作机器时差值为22个1，否则为12个1
		long expectedOffSet = ignore ? ~(-1 << 22) : ~(-1 << 12);
		System.out.println("计算差值 = " + trueOffSet + ", 预期差值 = " + expectedOffSet);
		Assert.assertEquals(trueOffSet, expectedOffSet);
	}

	/**
	 * long转雪花格式的2进制字符串
	 *
	 * @param number long值
	 * @return 符号位（1bit）- 时间戳相对值（41bit）- 数据中心标志（5bit）- 机器标志（5bit）- 递增序号（12bit）
	 */
	private String longToBinaryReadable(long number) {
		String binaryString = Long.toBinaryString(number);
		StringBuilder sb = new StringBuilder(binaryString);
		while (sb.length() < 64) {
			sb.insert(0, '0');  // 在二进制字符串前面补零
		}
		sb
				.insert(52, "-")
				.insert(47, "-")
				.insert(42, "-")
				.insert(1, "-")
		;
		return sb.toString();
	}

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
		Snowflake snowflake = IdUtil.getSnowflake(0, 0);

		Set<Long> ids = new ConcurrentHashSet<>();
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
			Assert.assertEquals(19, StrUtil.toString(l).length());
		}
	}

	@Test
	@Ignore
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
	@Ignore
	public void uniqueOfRandomSequenceTest(){
		// 测试并发环境下生成ID是否重复
		final Snowflake snowflake = new Snowflake(null, 0, 0,
				false, Snowflake.DEFAULT_TIME_OFFSET, 100);

		Set<Long> ids = new ConcurrentHashSet<>();
		ThreadUtil.concurrencyTest(100, () -> {
			for (int i = 0; i < 50000; i++) {
				if(false == ids.add(snowflake.nextId())){
					throw new UtilException("重复ID！");
				}
			}
		});
	}
}
