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

package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI75K5GTest {

	@Test
	@Disabled
	void appendTest() {
		final CsvWriter writer = CsvUtil.getWriter(
			FileUtil.file("d:/test/csvAppendTest.csv"),
			CharsetUtil.UTF_8, true,
			CsvWriteConfig.defaultConfig().setEndingLineBreak(true));

		writer.writeHeaderLine("name", "gender", "address");
		writer.writeLine("张三", "男", "XX市XX区");
		writer.writeLine("李四", "男", "XX市XX区,01号");

		writer.close();
	}

	@Test
	@Disabled
	void readTest() {
		final CsvReader reader = CsvUtil.getReader(FileUtil.getUtf8Reader("d:/test/csvAppendTest.csv"));
		final CsvData read = reader.read();
		for (final CsvRow row : read) {
			Console.log(row);
		}
	}
}
