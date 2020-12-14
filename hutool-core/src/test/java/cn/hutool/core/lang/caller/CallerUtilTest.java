package cn.hutool.core.lang.caller;

import org.junit.Assert;
import org.junit.Test;

public class CallerUtilTest {

	@Test
	public void getCallerMethodNameTest() {
		final String callerMethodName = CallerUtil.getCallerMethodName(false);
		Assert.assertEquals("getCallerMethodNameTest", callerMethodName);

		final String fullCallerMethodName = CallerUtil.getCallerMethodName(true);
		Assert.assertEquals("cn.hutool.core.lang.caller.CallerUtilTest.getCallerMethodNameTest", fullCallerMethodName);
	}
}