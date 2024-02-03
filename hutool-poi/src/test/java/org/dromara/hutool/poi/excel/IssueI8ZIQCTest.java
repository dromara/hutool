package org.dromara.hutool.poi.excel;

import org.junit.jupiter.api.Test;

public class IssueI8ZIQCTest {
	@Test
	void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/test3.xlsx", "aaa");
	}
}
