package cn.hutool.core.lang.caller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CallerUtilTest {

	@Test
	public void getCallerMethodNameTest() {
		final String callerMethodName = CallerUtil.getCallerMethodName(false);
		assertEquals("getCallerMethodNameTest", callerMethodName);

		final String fullCallerMethodName = CallerUtil.getCallerMethodName(true);
		assertEquals("cn.hutool.core.lang.caller.CallerUtilTest.getCallerMethodNameTest", fullCallerMethodName);
	}
}
