package cn.hutool.core.lang;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.thread.ThreadUtil;
import lombok.Data;
import org.junit.Test;

public class SingletonTest {

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
}
