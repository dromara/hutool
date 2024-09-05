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

import org.dromara.hutool.poi.excel.ExcelUtil;
import org.dromara.hutool.poi.excel.writer.ExcelWriter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue3481Test {
	@Test
	@Disabled
	void readerToWriterTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/aaa.xlsx");
		final ExcelWriter writer = reader.getWriter();
		writer.writeCellValue(3, 0, "原因");

		writer.close();
	}

	@Test
	@Disabled
	void readerToWriterTest2() {
		ExcelUtil.getReader("d:/test/aaa.xlsx")
			.getWriter().writeCellValue(3, 0, "原因")
			.close();
	}
}
