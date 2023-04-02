package org.dromara.hutool;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class HttpsTest {

	/**
	 * 测试单例的SSLSocketFactory是否有线程安全问题
	 */
	@Test
	@Disabled
	public void getTest() {
		final AtomicInteger count = new AtomicInteger();
		for(int i =0; i < 100; i++){
			ThreadUtil.execute(()->{
				final String s = HttpUtil.get("https://www.baidu.com/");
				Console.log(count.incrementAndGet());
			});
		}
		ThreadUtil.sync(this);
	}
}
