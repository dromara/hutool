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

package org.dromara.hutool.cron.pattern;

import org.dromara.hutool.cron.CronException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CronPatternBuilderTest {

	@Test
	public void buildMatchAllTest(){
		String build = CronPatternBuilder.of().build();
		Assertions.assertEquals("* * * * *", build);

		build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.build();
		Assertions.assertEquals("* * * * * *", build);

		build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.set(Part.YEAR, "*")
				.build();
		Assertions.assertEquals("* * * * * * *", build);
	}

	@Test
	public void buildRangeTest(){
		final String build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				.setRange(Part.HOUR, 2, 9)
				.build();
		Assertions.assertEquals("* * 2-9 * * *", build);
	}

	@Test
	public void buildRangeErrorTest(){
		Assertions.assertThrows(CronException.class, ()->{
			final String build = CronPatternBuilder.of()
				.set(Part.SECOND, "*")
				// 55无效值
				.setRange(Part.HOUR, 2, 55)
				.build();
			Assertions.assertEquals("* * 2-9 * * *", build);
		});
	}
}
