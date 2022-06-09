package cn.hutool.extra.management;

import org.junit.Assert;
import org.junit.Test;

/**
 * @see JavaInfo
 */
public class JavaInfoTest {

	@Test
	public void isJavaVersionAtLeastTest() {
		final int versionInt = ManagementUtil.getJavaInfo().getVersionIntSimple();
		Assert.assertTrue(versionInt >= 8);
		final boolean javaVersionAtLeast1 = ManagementUtil.getJavaInfo().isJavaVersionAtLeast(1.8f);
		Assert.assertTrue(javaVersionAtLeast1);
	}
}
