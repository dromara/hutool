package org.dromara.hutool.poi.excel;

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
