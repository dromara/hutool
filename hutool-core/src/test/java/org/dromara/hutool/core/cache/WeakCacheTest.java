package org.dromara.hutool.core.cache;

import org.dromara.hutool.core.cache.impl.WeakCache;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WeakCacheTest {

	@Test
	public void removeTest(){
		final WeakCache<String, String> cache = new WeakCache<>(-1);
		cache.put("abc", "123");
		cache.put("def", "456");

		Assertions.assertEquals(2, cache.size());

		// 检查被MutableObj包装的key能否正常移除
		cache.remove("abc");

		Assertions.assertEquals(1, cache.size());
	}

	@Test
	@Disabled
	public void removeByGcTest(){
		// https://gitee.com/dromara/hutool/issues/I51O7M
		final WeakCache<String, String> cache = new WeakCache<>(-1);
		cache.put("a", "1");
		cache.put("b", "2");

		// 监听
		Assertions.assertEquals(2, cache.size());
		cache.setListener(Console::log);

		// GC测试
		int i=0;
		while(true){
			if(2 == cache.size()){
				i++;
				Console.log("Object is alive for {} loops - ", i);
				System.gc();
			}else{
				Console.log("Object has been collected.");
				Console.log(cache.size());
				break;
			}
		}
	}
}
