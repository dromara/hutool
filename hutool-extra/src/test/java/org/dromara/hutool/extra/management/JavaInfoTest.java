package org.dromara.hutool.extra.management;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @see JavaInfo
 */
public class JavaInfoTest {

	@Test
	public void isJavaVersionAtLeastTest() {
		final int versionInt = ManagementUtil.getJavaInfo().getVersionIntSimple();
		Assertions.assertTrue(versionInt >= 8);
		final boolean javaVersionAtLeast1 = ManagementUtil.getJavaInfo().isJavaVersionAtLeast(1.8f);
		Assertions.assertTrue(javaVersionAtLeast1);
	}
}
