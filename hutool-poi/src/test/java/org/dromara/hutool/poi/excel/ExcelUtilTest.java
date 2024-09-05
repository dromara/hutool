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

package org.dromara.hutool.poi.excel;

import org.apache.poi.ss.util.CellReference;
import org.dromara.hutool.poi.excel.cell.CellReferenceUtil;
import org.dromara.hutool.poi.excel.reader.ExcelReader;
import org.dromara.hutool.poi.excel.writer.ExcelWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ExcelUtilTest {

	@Test
	public void indexToColNameTest() {
		Assertions.assertEquals("A", CellReferenceUtil.indexToColName(0));
		Assertions.assertEquals("B", CellReferenceUtil.indexToColName(1));
		Assertions.assertEquals("C", CellReferenceUtil.indexToColName(2));

		Assertions.assertEquals("AA", CellReferenceUtil.indexToColName(26));
		Assertions.assertEquals("AB", CellReferenceUtil.indexToColName(27));
		Assertions.assertEquals("AC", CellReferenceUtil.indexToColName(28));

		Assertions.assertEquals("AAA", CellReferenceUtil.indexToColName(702));
		Assertions.assertEquals("AAB", CellReferenceUtil.indexToColName(703));
		Assertions.assertEquals("AAC", CellReferenceUtil.indexToColName(704));
	}

	@Test
	public void colNameToIndexTest() {
		Assertions.assertEquals(704, CellReferenceUtil.colNameToIndex("AAC"));
		Assertions.assertEquals(703, CellReferenceUtil.colNameToIndex("AAB"));
		Assertions.assertEquals(702, CellReferenceUtil.colNameToIndex("AAA"));

		Assertions.assertEquals(28, CellReferenceUtil.colNameToIndex("AC"));
		Assertions.assertEquals(27, CellReferenceUtil.colNameToIndex("AB"));
		Assertions.assertEquals(26, CellReferenceUtil.colNameToIndex("AA"));

		Assertions.assertEquals(2, CellReferenceUtil.colNameToIndex("C"));
		Assertions.assertEquals(1, CellReferenceUtil.colNameToIndex("B"));
		Assertions.assertEquals(0, CellReferenceUtil.colNameToIndex("A"));
	}

	@Test
	public void cellReferenceTest() {
		final CellReference a11 = new CellReference("A11");
		Assertions.assertEquals(0, a11.getCol());
		Assertions.assertEquals(10, a11.getRow());
	}

	@Test
	public void readAndWriteTest() {
		final ExcelReader reader = ExcelUtil.getReader("aaa.xlsx");
		final ExcelWriter writer = reader.getWriter();
		writer.writeCellValue(1, 2, "设置值");
		writer.close();
	}

	@Test
	public void getReaderByBookFilePathAndSheetNameTest() {
		final ExcelReader reader = ExcelUtil.getReader("aaa.xlsx", "12");
		final List<Map<Object, Object>> list = reader.readAll();
		reader.close();
		Assertions.assertEquals(1L, list.get(1).get("鞋码"));
	}
}
