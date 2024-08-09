package cn.hutool.core.convert;

import cn.hutool.core.bean.BeanUtilTest.SubPerson;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.CaseInsensitiveMap;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类型转换工具单元测试<br>
 * 转换为数组
 *
 * @author Looly
 *
 */
public class ConvertToBeanTest {

	@Test
	public void beanToMapTest() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		Map<?, ?> map = Convert.convert(Map.class, person);
		assertEquals(map.get("name"), "测试A11");
		assertEquals(map.get("age"), 14);
		assertEquals("11213232", map.get("openid"));
	}

	@Test
	public void beanToMapTest2() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		Map<String, String> map = Convert.toMap(String.class, String.class, person);
		assertEquals("测试A11", map.get("name"));
		assertEquals("14", map.get("age"));
		assertEquals("11213232", map.get("openid"));

		final LinkedHashMap<String, String> map2 = Convert.convert(
				new TypeReference<LinkedHashMap<String, String>>() {}, person);
		assertEquals("测试A11", map2.get("name"));
		assertEquals("14", map2.get("age"));
		assertEquals("11213232", map2.get("openid"));
	}

	@Test
	public void mapToMapTest() {
		LinkedHashMap<String, Integer> map1 = new LinkedHashMap<>();
		map1.put("key1", 1);
		map1.put("key2", 2);
		map1.put("key3", 3);
		map1.put("key4", 4);

		Map<String, String> map2 = Convert.toMap(String.class, String.class, map1);

		assertEquals("1", map2.get("key1"));
		assertEquals("2", map2.get("key2"));
		assertEquals("3", map2.get("key3"));
		assertEquals("4", map2.get("key4"));
	}
	@Test
	public void mapToMapWithSelfTypeTest() {
		CaseInsensitiveMap<String, Integer> caseInsensitiveMap = new CaseInsensitiveMap<>();
		caseInsensitiveMap.put("jerry", 1);
		caseInsensitiveMap.put("Jerry", 2);
		caseInsensitiveMap.put("tom", 3);

		Map<String, String> map = Convert.toMap(String.class, String.class, caseInsensitiveMap);
		assertEquals("2", map.get("jerry"));
		assertEquals("2", map.get("Jerry"));
		assertEquals("3", map.get("tom"));
	}
	@Test
	public void beanToSpecifyMapTest() {
		SubPerson person = new SubPerson();
		person.setAge(14);
		person.setOpenid("11213232");
		person.setName("测试A11");
		person.setSubName("sub名字");

		Map<String, String> map = Convert.toMap(LinkedHashMap.class, String.class, String.class, person);
		assertEquals("测试A11", map.get("name"));
		assertEquals("14", map.get("age"));
		assertEquals("11213232", map.get("openid"));
	}
	@Test
	public void mapToBeanTest() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("id", "88dc4b28-91b1-4a1a-bab5-444b795c7ecd");
		map.put("age", 14);
		map.put("openid", "11213232");
		map.put("name", "测试A11");
		map.put("subName", "sub名字");

		SubPerson subPerson = Convert.convert(SubPerson.class, map);
		assertEquals("88dc4b28-91b1-4a1a-bab5-444b795c7ecd", subPerson.getId().toString());
		assertEquals(14, subPerson.getAge());
		assertEquals("11213232", subPerson.getOpenid());
		assertEquals("测试A11", subPerson.getName());
		assertEquals("11213232", subPerson.getOpenid());
	}

	@Test
	public void nullStrToBeanTest(){
		String nullStr = "null";
		final SubPerson subPerson = Convert.convertQuietly(SubPerson.class, nullStr);
		assertNull(subPerson);
	}
}
