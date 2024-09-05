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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * https://gitee.com/dromara/hutool/issues/IA8WE0
 */
public class IssueIA8WE0Test {
	@Test
	void csvReadTest() {
		final CsvReader csvReader = new CsvReader();
		final CsvData read = csvReader.read(FileUtil.file("issueIA8WE0.csv"));
		final List<CsvRow> rows = read.getRows();

		assertEquals(1, rows.size());
		assertEquals(3, rows.get(0).size());
		assertEquals("c1_text1", rows.get(0).get(0));
		// 如果\n#出现在双引号中，表示实际的文本内容，并不算注释
		assertEquals("c1_text2\n#c1_text2_line2", rows.get(0).get(1));
		assertEquals("c1_text3", rows.get(0).get(2));

		csvReader.close();
	}
}
