/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

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
