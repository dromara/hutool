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

public class BetweenFormatterTest {

	@Test
	public void formatTest(){
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2017-01-01 22:59:59"), DateUtil.parse("2017-01-02 23:59:58"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.MILLISECOND, 1);
		Assertions.assertEquals(formatter.toString(), "1天");
	}

	@Test
	public void formatBetweenTest(){
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 11:23:19"), DateUtil.parse("2018-07-16 11:23:20"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.SECOND, 1);
		Assertions.assertEquals(formatter.toString(), "1秒");
	}

	@Test
	public void formatBetweenTest2(){
		final long betweenMs = DateUtil.betweenMs(DateUtil.parse("2018-07-16 12:25:23"), DateUtil.parse("2018-07-16 11:23:20"));
		final BetweenFormatter formatter = new BetweenFormatter(betweenMs, BetweenFormatter.Level.SECOND, 5);
		Assertions.assertEquals(formatter.toString(), "1小时2分3秒");
	}

	@Test
	public void formatTest2(){
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
	}
}
