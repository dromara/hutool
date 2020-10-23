package cn.hutool.core.util;

import cn.hutool.core.clone.CloneSupport;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;

public class ObjectUtilTest {

	@Test
	public void cloneTest() {
		Obj obj = new Obj();
		Obj obj2 = ObjectUtil.clone(obj);
		Assert.assertEquals("OK", obj2.doSomeThing());
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
		Assert.assertEquals("[1, 2]", result);
	}

	@Test
	public void defaultIfNullTest() {
		final String nullValue = null;
		final String dateStr = "2020-10-23 15:12:30";
		Instant result1 = ObjectUtil.defaultIfNull(dateStr,
				() -> DateUtil.parse(dateStr, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		Assert.assertNotNull(result1);
		Instant result2 = ObjectUtil.defaultIfNull(nullValue,
				() -> DateUtil.parse(nullValue, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		Assert.assertNotNull(result2);
	}

	@Test
	public void defaultIfEmptyTest() {
		final String emptyValue = "";
		final String dateStr = "2020-10-23 15:12:30";
		Instant result1 = ObjectUtil.defaultIfEmpty(emptyValue,
				() -> DateUtil.parse(emptyValue, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		Assert.assertNotNull(result1);
		Instant result2 = ObjectUtil.defaultIfEmpty(dateStr,
				() -> DateUtil.parse(dateStr, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant.now());
		Assert.assertNotNull(result2);

	}
}
