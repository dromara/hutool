package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

public class PathUtilTest {

	@Test
	public void matchesTest() {
		boolean matches1 = PathUtil.matches("/api/**", "/api/test1/test2");
		Assert.assertTrue(matches1);

		boolean matches2 = PathUtil.matches("/api/*/test", "/api/test1/test2");
		Assert.assertTrue(matches2);
	}

}
