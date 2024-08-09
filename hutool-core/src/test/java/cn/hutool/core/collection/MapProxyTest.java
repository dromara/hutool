package cn.hutool.core.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cn.hutool.core.map.MapProxy;

public class MapProxyTest {

	@Test
	public void mapProxyTest() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");

		MapProxy mapProxy = new MapProxy(map);
		Integer b = mapProxy.getInt("b");
		assertEquals(new Integer(2), b);

		Set<Object> keys = mapProxy.keySet();
		assertFalse(keys.isEmpty());

		Set<Entry<Object,Object>> entrys = mapProxy.entrySet();
		assertFalse(entrys.isEmpty());
	}

	private interface Student {
		Student setName(String name);
		Student setAge(int age);

		String getName();
		int getAge();
	}

	@Test
	public void classProxyTest() {
		Student student = MapProxy.create(new HashMap<>()).toProxyBean(Student.class);
		student.setName("小明").setAge(18);
		assertEquals(student.getAge(), 18);
		assertEquals(student.getName(), "小明");
	}
}
