package cn.hutool.extra.spring;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SpringUtil.class, SpringUtilTest.Demo2.class})
//@Import(cn.hutool.extra.spring.SpringUtil.class)
public class SpringUtilTest {

	/**
	 * 注册bean
	 * 方便执行顺序使用abcd...的顺序命名方法
	 */
	@Test
	public void a() {
		Demo2 registerBean = new Demo2();
		registerBean.setId(123);
		registerBean.setName("222");
		SpringUtil.registerBean("registerBean", registerBean);
	}

	/**
	 * 验证注册的bean
	 */
	@Test
	public void b() {
		Demo2 registerBean = SpringUtil.getBean("registerBean");
		Assert.assertEquals(123, registerBean.getId());
		Assert.assertEquals("222", registerBean.getName());
	}
	@Test
	public void getBeanTest(){
		final Demo2 testDemo = SpringUtil.getBean("testDemo");
		Assert.assertEquals(12345, testDemo.getId());
		Assert.assertEquals("test", testDemo.getName());
	}

	@Test
	public void getBeanWithTypeReferenceTest() {
		Map<String, Object> mapBean = SpringUtil.getBean(new TypeReference<Map<String, Object>>() {});
		Assert.assertNotNull(mapBean);
		Assert.assertEquals("value1", mapBean.get("key1"));
		Assert.assertEquals("value2", mapBean.get("key2"));
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
}
