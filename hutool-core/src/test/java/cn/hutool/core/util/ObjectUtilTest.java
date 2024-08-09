package cn.hutool.core.util;

import cn.hutool.core.clone.CloneSupport;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtilTest {

	@Test
	public void equalsTest() {
		Object a = null;
		Object b = null;
		assertTrue(ObjectUtil.equals(a, b));
	}

	@Test
	public void lengthTest() {
		int[] array = new int[]{1, 2, 3, 4, 5};
		int length = ObjectUtil.length(array);
		assertEquals(5, length);

		Map<String, String> map = new HashMap<>();
		map.put("a", "a1");
		map.put("b", "b1");
		map.put("c", "c1");
		length = ObjectUtil.length(map);
		assertEquals(3, length);
	}

	@Test
	public void containsTest() {
		int[] array = new int[]{1, 2, 3, 4, 5};

		final boolean contains = ObjectUtil.contains(array, 1);
		assertTrue(contains);
	}

	@Test
	public void cloneTest() {
		Obj obj = new Obj();
		Obj obj2 = ObjectUtil.clone(obj);
		assertEquals("OK", obj2.doSomeThing());
	}

	static class Obj extends CloneSupport<Obj> {
		public String doSomeThing() {
			return "OK";
		}
	}

	@Test
	public void toStringTest() {
		ArrayList<String> strings = CollUtil.newArrayList("1", "2");
		String result = ObjectUtil.toString(strings);
		assertEquals("[1, 2]", result);
	}

	@Test
	public void defaultIfNullTest() {
		final String nullValue = null;
		final String dateStr = "2020-10-23 15:12:30";
		Instant result1 = ObjectUtil.defaultIfNull(dateStr,
				(source) -> DateUtil.parse(source, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		assertNotNull(result1);
		Instant result2 = ObjectUtil.defaultIfNull(nullValue,
				(source) -> DateUtil.parse(source, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		assertNotNull(result2);

		Obj obj = new Obj();
		Obj objNull = null;
		String result3 = ObjectUtil.defaultIfNull(obj, (a) -> obj.doSomeThing(), "fail");
		assertNotNull(result3);

		String result4 = ObjectUtil.defaultIfNull(objNull, Obj::doSomeThing, "fail");
		assertNotNull(result4);
	}

	@Test
	public void defaultIfEmptyTest() {
		final String emptyValue = "";
		final String dateStr = "2020-10-23 15:12:30";
		Instant result1 = ObjectUtil.defaultIfEmpty(emptyValue,
				(source) -> DateUtil.parse(source, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		assertNotNull(result1);
		Instant result2 = ObjectUtil.defaultIfEmpty(dateStr,
				(source) -> DateUtil.parse(source, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		assertNotNull(result2);
	}

	@Test
	public void isBasicTypeTest() {
		int a = 1;
		final boolean basicType = ObjectUtil.isBasicType(a);
		assertTrue(basicType);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void isNotNullTest() {
		String a = null;
		assertFalse(ObjectUtil.isNotNull(a));
	}
}
