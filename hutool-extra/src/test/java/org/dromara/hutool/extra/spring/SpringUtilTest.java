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

package org.dromara.hutool.extra.spring;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SpringUtil.class, SpringUtilTest.Demo2.class})
// @ActiveProfiles("dev") // SpringUtil.getActiveProfile()效果与下面方式一致
@TestPropertySource(properties = {"spring.profiles.active=dev"})
//@Import(spring.org.dromara.hutool.extra.SpringUtil.class)
public class SpringUtilTest {

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
		} catch (final NoSuchBeanDefinitionException e) {
			Assertions.assertEquals(e.getClass(), NoSuchBeanDefinitionException.class);
		}
	}

	/**
	 * 测试自动注入
	 */
	private void registerTestAutoWired() {
		TestAutoWired testAutoWired = new TestAutoWired();
		final TestBean testBean = new TestBean();
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
		final Map<String, Object> mapBean = SpringUtil.getBean(new TypeReference<Map<String, Object>>() {});
		Assertions.assertNotNull(mapBean);
		Assertions.assertEquals("value1", mapBean.get("key1"));
		Assertions.assertEquals("value2", mapBean.get("key2"));
	}

	@Test
	public void getActiveProfileTest() {
		final String activeProfile = SpringUtil.getActiveProfile();
		final String defaultProfile = SpringUtil.getProperty("spring.profiles.default");
		final String activeProfile2 = SpringUtil.getProperty("spring.profiles.active");
		assert "dev".equals(activeProfile);
		assert null == defaultProfile;
		assert "dev".equals(activeProfile2);
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
