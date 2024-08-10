/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
		final List<Map<String, Object>> list = reader.readAll();
		reader.close();
		Assertions.assertEquals(1L, list.get(1).get("鞋码"));
	}
}
