package cn.hutool.core.bean;

import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue3452Test {

	@Test
	public void fillBeanWithMapTest() {
		final Map<String, Object> properties = new HashMap<>();
		properties.put("name", "JohnDoe");
		properties.put("user_age", 25);
		final User user = BeanUtil.fillBeanWithMap(
			properties, new User(), CopyOptions.create());
		assertEquals("JohnDoe", user.getName());
		assertEquals(25, user.getUserAge());
	}

	@Data
	static class User {
		private String name;
		private int userAge;
	}
}
