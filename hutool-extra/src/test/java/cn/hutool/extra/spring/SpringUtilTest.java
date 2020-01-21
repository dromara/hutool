package cn.hutool.extra.spring;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SpringUtil.class, SpringUtilTest.Demo2.class})
//@Import(cn.hutool.extra.spring.SpringUtil.class)
public class SpringUtilTest {

	@Test
	public void getBeanTest(){
		final Demo2 testDemo = SpringUtil.getBean("testDemo");
		Assert.assertEquals(12345, testDemo.getId());
		Assert.assertEquals("test", testDemo.getName());
	}

	@Data
	public static class Demo2{
		private long id;
		private String name;

		@Bean(name="testDemo")
		public Demo2 generateDemo() {
			Demo2 demo = new Demo2();
			demo.setId(12345);
			demo.setName("test");
			return demo;
		}
	}
}
