package cn.hutool.json;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class IssueI6LBZATest {
	@Test
	public void parseJSONStringTest() {
		final String a = "\"a\"";
		final Object parse = JSONUtil.parse(a);
		Assert.assertEquals(String.class, parse.getClass());
	}

	@Test
	public void parseJSONStringTest2() {
		final String a = "'a'";
		final Object parse = JSONUtil.parse(a);
		Assert.assertEquals(String.class, parse.getClass());
	}

	@Test(expected = JSONException.class)
	public void parseJSONErrorTest() {
		final String a = "a";
		final Object parse = JSONUtil.parse(a);
		Assert.assertEquals(String.class, parse.getClass());
	}

	@Test
	public void parseJSONNumberTest() {
		final String a = "123";
		final Object parse = JSONUtil.parse(a);
		Assert.assertEquals(BigDecimal.class, parse.getClass());
	}
}
