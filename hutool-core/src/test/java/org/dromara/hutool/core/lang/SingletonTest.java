/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
		final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
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
