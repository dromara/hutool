package cn.hutool.extra.management;

import org.junit.Assert;
import org.junit.Test;

/**
 * @see JavaInfo
 */
public class JavaInfoTest {

	@Test
	public void isJavaVersionAtLeastTest() {
		boolean javaVersionAtLeast1 = ManagementUtil.getJavaInfo().isJavaVersionAtLeast(1.8f);
		Assert.assertTrue(javaVersionAtLeast1);

		boolean javaVersionAtLeast2 = ManagementUtil.getJavaInfo().isJavaVersionAtLeast(8f);
		Assert.assertTrue(javaVersionAtLeast2);
	}
}
