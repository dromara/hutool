package cn.hutool.cache;

import cn.hutool.cache.impl.WeakCache;
import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class WeakCacheTest {

	@Test
	public void removeTest(){
		final WeakCache<String, String> cache = new WeakCache<>(-1);
		cache.put("abc", "123");
		cache.put("def", "456");

		Assert.assertEquals(2, cache.size());

		// 检查被MutableObj包装的key能否正常移除
		cache.remove("abc");

		Assert.assertEquals(1, cache.size());
	}

	@Test
	@Ignore
	public void removeByGcTest(){
		// https://gitee.com/dromara/hutool/issues/I51O7M
		WeakCache<String, String> cache = new WeakCache<>(-1);
		cache.put("a", "1");
		cache.put("b", "2");

		// 监听
		Assert.assertEquals(2, cache.size());
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
