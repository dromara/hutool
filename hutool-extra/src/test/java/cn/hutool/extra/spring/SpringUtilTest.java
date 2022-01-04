package cn.hutool.extra.spring;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

@SpringBootTest(classes = {SpringUtilTest.Demo2.class})
@EnableAutoConfiguration
public class SpringUtilTest {

	/**
	 * 注册验证bean
	 */
	@Test
	public void registerBeanTest() {
		Demo2 registerBean = new Demo2();
		registerBean.setId(123);
		registerBean.setName("222");
		SpringUtil.registerBean("registerBean", registerBean);

		Demo2 registerBean2 = SpringUtil.getBean("registerBean");
		Assertions.assertEquals(123, registerBean2.getId());
		Assertions.assertEquals("222", registerBean2.getName());


	}

	/**
	 * 测试注销bean
	 */
	@Test
	public void unregisterBeanTest() {
		registerTestAutoWired();
		Assertions.assertNotNull(SpringUtil.getBean("testAutoWired"));
		SpringUtil.unregisterBean("testAutoWired1");
		try {
			SpringUtil.getBean("testAutoWired");
		} catch (NoSuchBeanDefinitionException e) {
			Assertions.assertEquals(e.getClass(), NoSuchBeanDefinitionException.class);
		}
	}

	/**
	 * 测试自动注入
	 */
	private void registerTestAutoWired() {
		TestAutoWired testAutoWired = new TestAutoWired();
		TestBean testBean = new TestBean();
		testBean.setId("123");
		SpringUtil.registerBean("testBean", testBean);
		SpringUtil.registerBean("testAutoWired", testAutoWired);

		testAutoWired = SpringUtil.getBean("testAutoWired");
		Assertions.assertNotNull(testAutoWired);
		Assertions.assertNotNull(testAutoWired.getAutowiredBean());
		Assertions.assertNotNull(testAutoWired.getResourceBean());
		Assertions.assertEquals("123", testAutoWired.getAutowiredBean().getId());

	}

	@Test
	public void getBeanTest(){
		final Demo2 testDemo = SpringUtil.getBean("testDemo");
		Assertions.assertEquals(12345, testDemo.getId());
		Assertions.assertEquals("test", testDemo.getName());
	}

	@Test
	public void getBeanWithTypeReferenceTest() {
		Map<String, Object> mapBean = SpringUtil.getBean(new TypeReference<Map<String, Object>>() {});
		Assertions.assertNotNull(mapBean);
		Assertions.assertEquals("value1", mapBean.get("key1"));
		Assertions.assertEquals("value2", mapBean.get("key2"));
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

		@Bean(name="mapDemo")
		public Map<String, Object> generateMap() {
			HashMap<String, Object> map = MapUtil.newHashMap();
			map.put("key1", "value1");
			map.put("key2", "value2");
			return map;
		}
	}

	@Data
	public static class TestAutoWired {

		@Autowired
		// @Resource
		private TestBean autowiredBean;

		 @Resource
		private TestBean resourceBean;
	}

	@Data
	public static class TestBean {
		private String id;
	}
}
