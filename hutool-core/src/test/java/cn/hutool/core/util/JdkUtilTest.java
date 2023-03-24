package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

public class JdkUtilTest {
	@Test
	public void jvmVersionTest() {
		final int jvmVersion = JdkUtil.JVM_VERSION;
		Assert.assertTrue(jvmVersion >= 8);
	}
}
