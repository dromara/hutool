package org.dromara.hutool.poi.excel.reader;

import org.dromara.hutool.poi.excel.ExcelReader;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI8ZIQCTest {
	@Test
	@Disabled
	void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/test3.xlsx", "aaa");
	}
}
