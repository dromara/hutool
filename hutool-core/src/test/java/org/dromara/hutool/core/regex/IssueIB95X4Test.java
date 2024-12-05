package org.dromara.hutool.core.regex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIB95X4Test {
	@Test
	void isMacTest() {
		Assertions.assertTrue(ReUtil.isMatch(PatternPool.MAC_ADDRESS, "ab1c.2d3e.f468"));
		Assertions.assertTrue(ReUtil.isMatch(PatternPool.MAC_ADDRESS, "ab:1c:2d:3e:f4:68"));
		Assertions.assertTrue(ReUtil.isMatch(PatternPool.MAC_ADDRESS, "ab-1c-2d-3e-f4-68"));
		Assertions.assertTrue(ReUtil.isMatch(PatternPool.MAC_ADDRESS, "ab1c2d3ef468"));
	}
}
