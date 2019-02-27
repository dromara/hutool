package cn.hutool.core.map;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;

public class MapUtilTest {

	@Test
	public void filterTest() {
		Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		Map<String, String> map2 = MapUtil.filter(map, new Filter<Entry<String, String>>() {

			@Override
			public boolean accept(Entry<String, String> t) {
				if (Convert.toInt(t.getValue()) % 2 == 0) {
					return true;
				}
				return false;
			}
		});

		Assert.assertEquals(2, map2.size());

		Assert.assertEquals("2", map2.get("b"));
		Assert.assertEquals("4", map2.get("d"));
	}

	@Test
	public void filterForEditorTest() {
		Map<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");
		map.put("d", "4");

		Map<String, String> map2 = MapUtil.filter(map, new Editor<Entry<String, String>>() {

			@Override
			public Entry<String, String> edit(Entry<String, String> t) {
				// 修改每个值使之*10
				t.setValue(t.getValue() + "0");
				return t;
			}
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
}
