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

package org.dromara.hutool.poi.excel.reader;

import lombok.Data;
import org.dromara.hutool.core.annotation.Alias;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class IssueI8PT9ZTest {

	@Test
	@Disabled
	void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/test.xlsx");
		final List<Map<Object, Object>> read = reader.read(2, 4, Integer.MAX_VALUE);
		for (final Map<Object, Object> stringObjectMap : read) {
			Console.log(stringObjectMap);
		}

		final List<PunchCard> read1 = reader.read(2, 4, PunchCard.class);
		for (final PunchCard punchCard : read1) {
			Console.log(punchCard);
		}
	}

	@Data
	static class PunchCard{
		@Alias("日期")
		private String date;
		@Alias("姓名")
		private String name;
	}
}
