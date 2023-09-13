package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class Issue3301Test {
	@Test
	void ofTest() {
		final ZonedDateTime now = ZonedDateTime.now();
		// 获得一个特殊的 temporal
		final String text = DateTimeFormatter.ISO_INSTANT.format(now);
		final TemporalAccessor temporal = DateTimeFormatter.ISO_INSTANT.parse(text);

		final LocalDateTime actual = TimeUtil.of(temporal);
		Assertions.assertEquals(now.toLocalDateTime().toString(), actual.toString());
	}
}
