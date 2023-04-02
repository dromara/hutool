package org.dromara.hutool.excel;

import org.dromara.hutool.excel.cell.CellLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ExcelUtilTest {

	@Test
	public void indexToColNameTest() {
		Assertions.assertEquals("A", ExcelUtil.indexToColName(0));
		Assertions.assertEquals("B", ExcelUtil.indexToColName(1));
		Assertions.assertEquals("C", ExcelUtil.indexToColName(2));

		Assertions.assertEquals("AA", ExcelUtil.indexToColName(26));
		Assertions.assertEquals("AB", ExcelUtil.indexToColName(27));
		Assertions.assertEquals("AC", ExcelUtil.indexToColName(28));

		Assertions.assertEquals("AAA", ExcelUtil.indexToColName(702));
		Assertions.assertEquals("AAB", ExcelUtil.indexToColName(703));
		Assertions.assertEquals("AAC", ExcelUtil.indexToColName(704));
	}

	@Test
	public void colNameToIndexTest() {
		Assertions.assertEquals(704, ExcelUtil.colNameToIndex("AAC"));
		Assertions.assertEquals(703, ExcelUtil.colNameToIndex("AAB"));
		Assertions.assertEquals(702, ExcelUtil.colNameToIndex("AAA"));

		Assertions.assertEquals(28, ExcelUtil.colNameToIndex("AC"));
		Assertions.assertEquals(27, ExcelUtil.colNameToIndex("AB"));
		Assertions.assertEquals(26, ExcelUtil.colNameToIndex("AA"));

		Assertions.assertEquals(2, ExcelUtil.colNameToIndex("C"));
		Assertions.assertEquals(1, ExcelUtil.colNameToIndex("B"));
		Assertions.assertEquals(0, ExcelUtil.colNameToIndex("A"));
	}

	@Test
	public void toLocationTest() {
		final CellLocation a11 = ExcelUtil.toLocation("A11");
		Assertions.assertEquals(0, a11.getX());
		Assertions.assertEquals(10, a11.getY());
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
