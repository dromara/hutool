package org.dromara.hutool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI6LBZATest {
	@Test
	public void parseJSONStringTest() {
		final String a = "\"a\"";
		final Object parse = JSONUtil.parse(a);
		Assertions.assertEquals(String.class, parse.getClass());
	}

	@Test
	public void parseJSONStringTest2() {
		final String a = "'a'";
		final Object parse = JSONUtil.parse(a);
		Assertions.assertEquals(String.class, parse.getClass());
	}

	@Test
	public void parseJSONErrorTest() {
		Assertions.assertThrows(JSONException.class, ()->{
			final String a = "a";
			final Object parse = JSONUtil.parse(a);
			Assertions.assertEquals(String.class, parse.getClass());
		});
	}

	@Test
	public void parseJSONNumberTest() {
		final String a = "123";
		final Object parse = JSONUtil.parse(a);
		Assertions.assertEquals(Integer.class, parse.getClass());
	}
}
