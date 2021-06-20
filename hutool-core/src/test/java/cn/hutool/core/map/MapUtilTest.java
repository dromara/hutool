package cn.hutool.core.map;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapUtilTest {

	@Test
	public void filterTest() {
		Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		Map<String, String> map2 = MapUtil.filter(map, t -> Convert.toInt(t.getValue()) % 2 == 0);

		Assert.assertEquals(2, map2.size());

		Assert.assertEquals("2", map2.get("b"));
		Assert.assertEquals("4", map2.get("d"));
	}

	@Test
	public void filterContainsTest() {
		Map<String, String> map = MapUtil.newHashMap();
		map.put("abc", "1");
		map.put("bcd", "2");
		map.put("def", "3");
		map.put("fgh", "4");

		Map<String, String> map2 = MapUtil.filter(map, t -> StrUtil.contains(t.getKey(), "bc"));
		Assert.assertEquals(2, map2.size());
		Assert.assertEquals("1", map2.get("abc"));
		Assert.assertEquals("2", map2.get("bcd"));
	}

	@Test
	public void editTest() {
		Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		Map<String, String> map2 = MapUtil.edit(map, t -> {
			// 修改每个值使之*10
			t.setValue(t.getValue() + "0");
			return t;
		});

		Assert.assertEquals(4, map2.size());

		Assert.assertEquals("10", map2.get("a"));
		Assert.assertEquals("20", map2.get("b"));
		Assert.assertEquals("30", map2.get("c"));
		Assert.assertEquals("40", map2.get("d"));
	}

	@Test
	public void reverseTest() {
		Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		Map<String, String> map2 = MapUtil.reverse(map);

		Assert.assertEquals("a", map2.get("1"));
		Assert.assertEquals("b", map2.get("2"));
		Assert.assertEquals("c", map2.get("3"));
		Assert.assertEquals("d", map2.get("4"));
	}

	@Test
	public void toObjectArrayTest() {
		Map<String, String> map = MapUtil.newHashMap(true);
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		Object[][] objectArray = MapUtil.toObjectArray(map);
		Assert.assertEquals("a", objectArray[0][0]);
		Assert.assertEquals("1", objectArray[0][1]);
		Assert.assertEquals("b", objectArray[1][0]);
		Assert.assertEquals("2", objectArray[1][1]);
		Assert.assertEquals("c", objectArray[2][0]);
		Assert.assertEquals("3", objectArray[2][1]);
		Assert.assertEquals("d", objectArray[3][0]);
		Assert.assertEquals("4", objectArray[3][1]);
	}

	@Test
	public void sortJoinTest(){
		Map<String, String> build = MapUtil.builder(new HashMap<String, String>())
				.put("key1", "value1")
				.put("key3", "value3")
				.put("key2", "value2").build();

		String join1 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false);
		Assert.assertEquals("key1value1key2value2key3value3", join1);

		String join2 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123");
		Assert.assertEquals("key1value1key2value2key3value3123", join2);

		String join3 = MapUtil.sortJoin(build, StrUtil.EMPTY, StrUtil.EMPTY, false, "123", "abc");
		Assert.assertEquals("key1value1key2value2key3value3123abc", join3);
	}
}
