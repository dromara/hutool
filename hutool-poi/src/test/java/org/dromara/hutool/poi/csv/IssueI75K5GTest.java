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
