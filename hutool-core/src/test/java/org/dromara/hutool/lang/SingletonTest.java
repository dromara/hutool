package org.dromara.hutool.lang;

import org.dromara.hutool.exceptions.UtilException;
import org.dromara.hutool.thread.ThreadUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

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
				throw new UtilException("单例测试中，对象被创建了两次！");
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
}
