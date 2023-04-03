package org.dromara.hutool.extra.spring;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SpringUtilWithAutoConfigTest.Demo2.class})
//@Import(spring.org.dromara.hutool.extra.SpringUtil.class)
@EnableAutoConfiguration
public class SpringUtilWithAutoConfigTest {

	/**
	 * 注册验证bean
	 */
	@Test
	public void registerBeanTest() {
		final Demo2 registerBean = new Demo2();
		registerBean.setId(123);
		registerBean.setName("222");
		SpringUtil.registerBean("registerBean", registerBean);

		final Demo2 registerBean2 = SpringUtil.getBean("registerBean");
		Assertions.assertEquals(123, registerBean2.getId());
		Assertions.assertEquals("222", registerBean2.getName());
	}

	@Test
	public void getBeanTest(){
		final Demo2 testDemo = SpringUtil.getBean("testDemo");
		Assertions.assertEquals(12345, testDemo.getId());
		Assertions.assertEquals("test", testDemo.getName());
	}

	@Test
	public void getBeanWithTypeReferenceTest() {
		final Map<String, Object> mapBean = SpringUtil.getBean(new TypeReference<Map<String, Object>>() {});
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
			final Demo2 demo = new Demo2();
			demo.setId(12345);
			demo.setName("test");
			return demo;
		}

		@Bean(name="mapDemo")
		public Map<String, Object> generateMap() {
			final HashMap<String, Object> map = MapUtil.newHashMap();
			map.put("key1", "value1");
			map.put("key2", "value2");
			return map;
		}
	}
}
