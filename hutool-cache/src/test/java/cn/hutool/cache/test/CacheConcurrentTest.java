package cn.hutool.cache.test;

import java.util.Iterator;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.thread.ThreadUtil;

/**
 * 缓存单元测试
 * @author looly
 *
 */
public class CacheConcurrentTest {
	
	public static void main(String[] args) {
		final Cache<String, String> fifoCache = new FIFOCache<>(3);

		// 由于缓存容量只有3，当加入第四个元素的时候，根据FIFO规则，最先放入的对象将被移除

		for (int i = 0; i < 4000; i++) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					fifoCache.put("key1", "value1", System.currentTimeMillis() * 3);
					fifoCache.put("key2", "value2", System.currentTimeMillis() * 3);
					fifoCache.put("key3", "value3", System.currentTimeMillis() * 3);
					fifoCache.put("key4", "value4", System.currentTimeMillis() * 3);
					ThreadUtil.sleep(1000);
					fifoCache.put("key5", "value5", System.currentTimeMillis() * 3);
					fifoCache.put("key6", "value6", System.currentTimeMillis() * 3);
					fifoCache.put("key7", "value7", System.currentTimeMillis() * 3);
					fifoCache.put("key8", "value8", System.currentTimeMillis() * 3);
					System.out.println("put all");
				}
			}).start();
		}

		for (int i = 0; i < 4000; i++) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					show(fifoCache);
				}
			}).start();
		}

		System.out.println("==============================");
		ThreadUtil.sleep(10000);

	}

	private static void show(Cache<String, String> fifoCache) {
		Iterator<?> its = fifoCache.iterator();

		while (its.hasNext()) {
			Object tt = its.next();
			System.out.println(tt);
		}
	}
}
