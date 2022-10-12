package cn.hutool.core.convert;

import cn.hutool.core.map.MapBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map转换单元测试
 *
 * @author looly
 *
 */
public class MapConvertTest {

	@Test
	public void beanToMapTest() {
		final User user = new User();
		user.setName("AAA");
		user.setAge(45);

		final HashMap<?, ?> map = Convert.convert(HashMap.class, user);
		Assert.assertEquals("AAA", map.get("name"));
		Assert.assertEquals(45, map.get("age"));
	}

	@Test
	public void mapToMapTest() {
		final Map<String, Object> srcMap = MapBuilder
				.create(new HashMap<String, Object>())
				.put("name", "AAA")
				.put("age", 45).map();

		final LinkedHashMap<?, ?> map = Convert.convert(LinkedHashMap.class, srcMap);
		Assert.assertEquals("AAA", map.get("name"));
		Assert.assertEquals(45, map.get("age"));
	}

	public static class User {
		private String name;
		private int age;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(final int age) {
			this.age = age;
		}
	}
}
