package cn.hutool.poi.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueIB0EJ9Test {
	@Test
	@Disabled
	void saxReadTest() {
		ExcelUtil.readBySax(FileUtil.file("d:/test/数值型测试.xlsx"), "hcm工资表",
			(sheetIndex, rowIndex, rowlist) -> Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist));
	}
}
