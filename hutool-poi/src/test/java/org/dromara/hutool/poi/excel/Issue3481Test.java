package org.dromara.hutool.poi.excel;

import org.dromara.hutool.core.io.file.FileUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue3481Test {
	@Test
	@Disabled
	void readerToWriterTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/aaa.xlsx");
		// 写出
		final ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file("d:/test/aaa.xlsx"));
		writer.getOrCreateRow(10).createCell(0).setCellValue("测试");
		writer.flush(FileUtil.file("d:/test/bbb.xlsx"));
		writer.close();
	}
}
