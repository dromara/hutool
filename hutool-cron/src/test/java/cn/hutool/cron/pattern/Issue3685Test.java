package cn.hutool.cron.pattern;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3685Test {
	@Test
	public void nextDateAfterTest() {
		Date date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-01"));
		assertEquals("2024-08-05 00:00:00", date.toString());

		date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-02"));
		assertEquals("2024-08-05 00:00:00", date.toString());

		date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-03"));
		assertEquals("2024-08-05 00:00:00", date.toString());

		date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-04"));
		assertEquals("2024-08-05 00:00:00", date.toString());

		date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-05"));
		assertEquals("2024-08-12 00:00:00", date.toString());
	}
}
