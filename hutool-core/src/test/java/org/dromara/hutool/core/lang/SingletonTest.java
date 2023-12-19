/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.thread.ThreadUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SingletonTest {

	@SuppressWarnings("resource")
	@Test
	public void getTest(){
		// 此测试中使用1000个线程获取单例对象，其间对象只被创建一次
		ThreadUtil.concurrencyTest(1000, ()-> Singleton.get(TestBean.class));
	}

	@Data
	static class TestBean{
		private static volatile TestBean testSingleton;

		public TestBean(){
			if(null != testSingleton){
				throw new HutoolException("单例测试中，对象被创建了两次！");
			}
			testSingleton = this;
		}

		private String name;
		private String age;
	}

	/**
	 * 测试单例构建属性锁死问题
	 * C构建单例时候，同时构建B，此时在SimpleCache中会有写锁竞争（写入C时获取了写锁，此时要写入B，也要获取写锁）
	 */
	@Test
	public void reentrantTest(){
		Assertions.assertTimeout(Duration.ofMillis(1000L), ()->{
			final C c = Singleton.get(C.class);
			Assertions.assertEquals("aaa", c.getB().getA());
		});
	}

	@Data
	static class B{
		private String a = "aaa";
	}

	@Data
	static class C{
		private B b = Singleton.get(B.class);
	}

	@Test
	@Disabled
	void issue3435Test() {
		final String key = "123";
		final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		for (int i = 0; i < 100; i++) {
			threadPoolExecutor.execute(() -> {
				Singleton.get(key, () -> {
					System.out.println(key);
					return "123";
				});
			});
		}

		ThreadUtil.sleep(5000);
	}
}
