package org.dromara.hutool.poi.excel.reader;

import org.dromara.hutool.poi.excel.ExcelReader;
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
