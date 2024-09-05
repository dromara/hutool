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
