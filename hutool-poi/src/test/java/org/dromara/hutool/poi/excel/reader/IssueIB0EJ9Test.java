package org.dromara.hutool.poi.excel.reader;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueIB0EJ9Test {
	@Test
	@Disabled
	void saxReadTest() {
		ExcelUtil.readBySax(FileUtil.file("d:/test/bbb.xlsx"), "Sheet1",
			(sheetIndex, rowIndex, rowlist) -> Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist));
	}
}
