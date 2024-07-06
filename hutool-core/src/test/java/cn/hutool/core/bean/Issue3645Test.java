package cn.hutool.core.bean;

import lombok.Data;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class Issue3645Test {
	@Test
	public void copyPropertiesTest() {
		User p = new User();
		p.setUserId(123L);

		Map<Long, User> map = new HashMap<>();
		map.put(123L,p);

		Map<Long, User> m = new HashMap<>();
		BeanUtil.copyProperties(map, m);
		User u = m.get(123L);
		assertNotNull(u);
	}

	@Data
	static class User{
		private Long userId;
	}
}
