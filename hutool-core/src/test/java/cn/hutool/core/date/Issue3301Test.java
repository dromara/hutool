package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class Issue3301Test {
	@Test
	public void ofTest() {
		final ZonedDateTime now = ZonedDateTime.now();
		// 获得一个特殊的 temporal
		String text = DateTimeFormatter.ISO_INSTANT.format(now);
		TemporalAccessor temporal = DateTimeFormatter.ISO_INSTANT.parse(text);

		LocalDateTime actual = LocalDateTimeUtil.of(temporal);
		Assert.assertEquals(now.toLocalDateTime().toString(), actual.toString());
	}
}
