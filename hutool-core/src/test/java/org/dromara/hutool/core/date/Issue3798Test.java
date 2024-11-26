package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3798Test {
	@Test
	void parseTest() {
		final String iso_datetime1 = "2000-01-01T12:00:00+08:00";
		final DateTime parse1 = DateUtil.parse(iso_datetime1);
		Assertions.assertEquals("2000-01-01 12:00:00", parse1.toString());

		// 伦敦时间（Greenwich Mean Time, GMT）和北京时间（China Standard Time, CST）之间的时差是8小时。北京时间比伦敦时间快8小时
		final String iso_datetime2 = "2000-01-01T12:00:00+00:00";
		final DateTime parse2 = DateUtil.parse(iso_datetime2);
		Assertions.assertEquals("2000-01-01 20:00:00", parse2.toString());
	}
}
