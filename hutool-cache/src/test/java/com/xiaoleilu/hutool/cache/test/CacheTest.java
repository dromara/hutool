package com.xiaoleilu.hutool.cache.test;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.cache.Cache;
import com.xiaoleilu.hutool.cache.CacheUtil;
import com.xiaoleilu.hutool.cache.impl.TimedCache;
import com.xiaoleilu.hutool.date.DateUnit;
import com.xiaoleilu.hutool.thread.ThreadUtil;

/**
 * 缓存测试用例
 * @author Looly
 *
 */
public class CacheTest {
	
	@Test
	public void fifoCacheTest(){
		Cache<String,String> fifoCache = CacheUtil.newFIFOCache(3);
		fifoCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		fifoCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);
		
		//由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除
		String value1 = fifoCache.get("key1");
		Assert.assertTrue(null == value1);
	}
	
	@Test
	public void lfuCacheTest(){
		Cache<String, String> lfuCache = CacheUtil.newLFUCache(3);
		lfuCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		//使用次数+1
		lfuCache.get("key1"); 
		lfuCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		lfuCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		lfuCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);
		
		//由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2,3被移除）
		String value2 = lfuCache.get("key2");
		String value3 = lfuCache.get("key3");
		Assert.assertTrue(null == value2);
		Assert.assertTrue(null == value3);
	}
	
	@Test
	public void lruCacheTest(){
		Cache<String, String> lruCache = CacheUtil.newLRUCache(3);
		//通过实例化对象创建
//		LRUCache<String, String> lruCache = new LRUCache<String, String>(3);
		lruCache.put("key1", "value1", DateUnit.SECOND.getMillis() * 3);
		lruCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 3);
		lruCache.put("key3", "value3", DateUnit.SECOND.getMillis() * 3);
		//使用时间推近
		lruCache.get("key1");
		lruCache.put("key4", "value4", DateUnit.SECOND.getMillis() * 3);
		
		//由于缓存容量只有3，当加入第四个元素的时候，根据LRU规则，最少使用的将被移除（2被移除）
		String value2 = lruCache.get("key2");
		Assert.assertTrue(null == value2);
	}
	
	@Test
	public void timedCacheTest(){
		TimedCache<String, String> timedCache = CacheUtil.newTimedCache(DateUnit.SECOND.getMillis() * 3);
//		TimedCache<String, String> timedCache = new TimedCache<String, String>(DateUnit.SECOND.getMillis() * 3);
		timedCache.put("key1", "value1", 1);
		timedCache.put("key2", "value2", DateUnit.SECOND.getMillis() * 5);
		
		//启动定时任务，每5毫秒秒检查一次过期
		timedCache.schedulePrune(5);
		
		ThreadUtil.sleep(5);
		
		//5毫秒后由于value2设置了5毫秒过期，因此只有value2被保留下来
		String value1 = timedCache.get("key1");
		String value2 = timedCache.get("key2");
		Assert.assertTrue(null == value1);
		Assert.assertFalse(null == value2);
		
		//取消定时清理
		timedCache.cancelPruneSchedule();
	}
}
