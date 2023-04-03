package org.dromara.hutool.core.lang.caller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallerUtilTest {

	@Test
	public void getCallerMethodNameTest() {
		final String callerMethodName = CallerUtil.getCallerMethodName(false);
		Assertions.assertEquals("getCallerMethodNameTest", callerMethodName);

		final String fullCallerMethodName = CallerUtil.getCallerMethodName(true);
		Assertions.assertEquals("org.dromara.hutool.core.lang.caller.CallerUtilTest.getCallerMethodNameTest", fullCallerMethodName);
	}
}
