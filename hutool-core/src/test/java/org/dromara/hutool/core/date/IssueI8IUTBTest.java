package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI8IUTBTest {
	@Test
	@Disabled
	void parseTest() {
		final DateTime parse = DateUtil.parse("May 8, 2009 5:57:51 PM");
		Console.log(parse);
	}
}
