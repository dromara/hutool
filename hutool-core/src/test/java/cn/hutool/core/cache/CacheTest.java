package cn.hutool.core.cache;

import cn.hutool.core.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 缓存测试用例
 * @author Looly
 *
 */
public class CacheTest {

	@Test
	public void fifoCacheTest(){
		final Cache<String,String> fifoCache = CacheUtil.newFIFOCache(3);
		fifoCache.setListener((key, value)->{
			// 监听测试，此测试中只有key1被移除，测试是否监听成功
			Assertions.assertEquals("key1", key);
			Assertions.assertEquals("value1", value);
		});

		fifoCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

		//由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除
		final String value1 = fifoCache.get("key1");
		Assertions.assertNull(value1);
	}

	@Test
	public void fifoCacheCapacityTest(){
		final Cache<String,String> fifoCache = CacheUtil.newFIFOCache(100);
		for (int i = 0; i < RandomUtil.randomInt(100, 1000); i++) {
			fifoCache.put("key" + i, "value" + i);
		}
		Assertions.assertEquals(100, fifoCache.size());
	}

	@Test
	public void lfuCacheTest(){
		final Cache<String, String> lfuCache = CacheUtil.newLFUCache(3);
		lfuCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		//使用次数+1
		lfuCache.get("key1");
		lfuCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		lfuCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		lfuCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

		//由于缓存容量只有3，当加入第四个元素的时候，根据LFU规则，最少使用的将被移除（2,3被移除）
		final String value1 = lfuCache.get("key1");
		final String value2 = lfuCache.get("key2");
		final String value3 = lfuCache.get("key3");
		Assertions.assertNotNull(value1);
		Assertions.assertNull(value2);
		Assertions.assertNull(value3);
	}

	@Test
	public void lfuCacheTest2(){
		final Cache<String, String> lfuCache = CacheUtil.newLFUCache(3);
		final String s = lfuCache.get(null);
		Assertions.assertNull(s);
	}

	@Test
	public void lruCacheTest(){
		final Cache<String, String> lruCache = CacheUtil.newLRUCache(3);
		//通过实例化对象创建
//		LRUCache<String, String> lruCache = new LRUCache<String, String>(3);
		lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		//使用时间推近
		lruCache.get("key1");
		lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);

		final String value1 = lruCache.get("key1");
		Assertions.assertNotNull(value1);
		//由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
		final String value2 = lruCache.get("key2");
		Assertions.assertNull(value2);
	}

	@Test
	public void timedCacheTest(){
		final TimedCache<String, String> timedCache = CacheUtil.newTimedCache(4);
//		TimedCache<String, String> timedCache = new TimedCache<String, String>(DateUnit.SECOND.getMillis() * 3);
		timedCache.put("key1", "value1", 1);//1毫秒过期
		timedCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 5);//5秒过期
		timedCache.put("key3", "value3");//默认过期(4毫秒)
		timedCache.put("key4", "value4", Long.MAX_VALUE);//永不过期

		//启动定时任务，每5毫秒秒检查一次过期
		timedCache.schedulePrune(5);
		//等待5毫秒
		ThreadUtil.sleep(5);

		//5毫秒后由于value2设置了5毫秒过期，因此只有value2被保留下来
		final String value1 = timedCache.get("key1");
		Assertions.assertNull(value1);
		final String value2 = timedCache.get("key2");
		Assertions.assertEquals("value2", value2);

		//5毫秒后，由于设置了默认过期，key3只被保留4毫秒，因此为null
		final String value3 = timedCache.get("key3");
		Assertions.assertNull(value3);

		final String value3Supplier = timedCache.get("key3", () -> "Default supplier");
		Assertions.assertEquals("Default supplier", value3Supplier);

		// 永不过期
		final String value4 = timedCache.get("key4");
		Assertions.assertEquals("value4", value4);

		//取消定时清理
		timedCache.cancelPruneSchedule();
	}
}
