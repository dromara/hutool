/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class BetweenFormatterTest {

	Function<BetweenFormatter.Level, String> levelFormatterEn = level -> {
		switch (level) {
			case DAY:
				return " day";
			case HOUR:
				return " hour";
			case MINUTE:
				return " minute";
			case SECOND:
				return " second";
			case MILLISECOND:
				return " millisecond";
			default:
				return " " + level.name();
		}
	};

	@Test
	public void formatTest() {
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.MILLISECOND, 1);
		Assertions.assertEquals(formatter.toString(), "1天");
	}

	@Test
	public void formatTestEn() {
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.MILLISECOND, 1);
		formatter.setLevelFormatter(levelFormatterEn);
		Assertions.assertEquals(formatter.toString(), "1 day");
	}

	@Test
	public void formatBetweenTest() {
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 11:23:19"), DateUtil.parse("2018-07-16 11:23:20"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.SECOND, 1);
		Assertions.assertEquals(formatter.toString(), "1秒");
	}

	@Test
	public void formatBetweenTest2() {
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 12:25:23"), DateUtil.parse("2018-07-16 11:23:20"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.SECOND, 5);
		Assertions.assertEquals(formatter.toString(), "1小时2分3秒");
	}

	@Test
	public void formatTest2() {
		final BetweenFormatter formatter = new BetweenFormatter(584, BetweenFormatter.Level.SECOND, 1);
		Assertions.assertEquals(formatter.toString(), "0秒");
	}

	@Test
	void issueI88LB8Test() {
		String s = BetweenFormatter.of(3609000, BetweenFormatter.Level.SECOND).setSimpleMode(false).format();
		Assertions.assertEquals("1小时0分9秒", s);

		s = BetweenFormatter.of(9000, BetweenFormatter.Level.MILLISECOND).setSimpleMode(false).format();
		Assertions.assertEquals("9秒", s);

		s = BetweenFormatter.of(3600000, BetweenFormatter.Level.MILLISECOND).setSimpleMode(false).format();
		Assertions.assertEquals("1小时0分0秒", s);

		s = BetweenFormatter.of(3600000, BetweenFormatter.Level.MILLISECOND).setSimpleMode(false).setLevelFormatter(levelFormatterEn).setSeparator(",").format();
		Assertions.assertEquals("1 hour,0 minute,0 second", s);
	}
}
