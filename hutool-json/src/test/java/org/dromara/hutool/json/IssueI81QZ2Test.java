package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI81QZ2Test {
	@Test
	void toJsonStrTest() {
		final boolean bool = true;
		Assertions.assertEquals("true", JSONUtil.toJsonStr(bool));
	}
}
