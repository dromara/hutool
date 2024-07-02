package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class IssueI8IUTBTest {
	@Test
	void parseTest() {
		final Date parse = DateUtil.parse("May 8, 2009 5:57:51 PM");
		Console.log(parse);
	}
}
