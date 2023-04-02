package org.dromara.hutool.util;

import org.dromara.hutool.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JdkUtilTest {
	@Test
	public void jvmVersionTest() {
		final int jvmVersion = JdkUtil.JVM_VERSION;
		Assertions.assertTrue(jvmVersion >= 8);
	}

	@Test
	@Disabled
	public void getJvmNameTest() {
		Console.log(JdkUtil.IS_AT_LEAST_JDK17);
	}
}
