package cn.hutool.core.collection;

import cn.hutool.core.map.MapProxy;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapProxyTest {

	@Test
	public void mapProxyTest() {
		final Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");

		final MapProxy mapProxy = new MapProxy(map);
		final Integer b = mapProxy.getInt("b");
		Assert.assertEquals(new Integer(2), b);

		final Set<Object> keys = mapProxy.keySet();
		Assert.assertFalse(keys.isEmpty());

		final Set<Entry<Object,Object>> entrys = mapProxy.entrySet();
		//noinspection ConstantConditions
		Assert.assertFalse(entrys.isEmpty());
	}

	private interface Student {
		Student setName(String name);
		Student setAge(int age);

		String getName();
		int getAge();
	}

	@Test
	public void classProxyTest() {
		final Student student = MapProxy.of(new HashMap<>()).toProxyBean(Student.class);
		student.setName("小明").setAge(18);
		Assert.assertEquals(student.getAge(), 18);
		Assert.assertEquals(student.getName(), "小明");
	}
}
