package cn.hutool.core.util;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class JdkUtilTest {
	@Test
	public void jvmVersionTest() {
		final int jvmVersion = JdkUtil.JVM_VERSION;
		Assert.assertTrue(jvmVersion >= 8);
	}

	@Test
	@Ignore
	public void getJvmNameTest() {
		Console.log(JdkUtil.IS_AT_LEAST_JDK17);
	}
}
