/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.cron.pattern;

import cn.hutool.cron.pattern.matcher.PatternMatcher;
import cn.hutool.cron.pattern.parser.PatternParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class IssueI7SMP7Test {
	@Test
	public void parseTest() {
		final List<PatternMatcher> parse = PatternParser.parse("0 0 3 1 1 ? */1");
		Assert.assertNotNull(parse);
	}
}
