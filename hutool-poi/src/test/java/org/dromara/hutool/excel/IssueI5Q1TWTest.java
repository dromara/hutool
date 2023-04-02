package org.dromara.hutool.excel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI5Q1TWTest {

	@Test
	public void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("I5Q1TW.xlsx");

		// 自定义时间格式1
		Assertions.assertEquals("18:56", reader.readCellValue(0, 0).toString());

		// 自定义时间格式2
		Assertions.assertEquals("18:56", reader.readCellValue(1, 0).toString());
	}
}
