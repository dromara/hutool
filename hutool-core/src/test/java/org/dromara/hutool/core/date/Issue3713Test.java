package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3713Test {
	@Test
	void parseTest() {
		final DateTime parse = DateUtil.parse("Aug 22, 2024, 4:21:21 PM");
		Assertions.assertEquals("2024-08-22 16:21:21", parse.toString());
	}
}
