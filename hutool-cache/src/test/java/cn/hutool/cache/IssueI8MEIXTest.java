package cn.hutool.cache;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Ignore;
import org.junit.Test;

public class IssueI8MEIXTest {

	@Test
	@Ignore
	public void getRemoveTest() {
		final TimedCache<String, String> cache = new TimedCache<>(200);
		cache.put("a", "123");

		ThreadUtil.sleep(300);

		// 测试时，在get后的remove前加sleep测试在读取过程中put新值的问题
		ThreadUtil.execute(()->{
			Console.log(cache.get("a"));
		});

		ThreadUtil.execute(()->{
			cache.put("a", "456");
		});

		ThreadUtil.sleep(1000);
	}
}
