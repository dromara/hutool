package org.dromara.hutool.poi.excel.writer;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Test;

public class TemplateWriterTest {
	@Test
	void writeRowTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/template.xlsx");

		// 单个替换的变量
		writer.fillRow(MapUtil
			.builder("date", (Object)"2024-01-01")
			.build());

		// 列表替换
		for (int i = 0; i < 10; i++) {
			writer.fillRow(MapUtil
				.builder("user.name", (Object)"张三")
				.put("user.age", 18)
				.put("year", 2024)
				.put("month", 8)
				.put("day", 24)
				.put("day", 24)
				.put("user.area123", "某某市")
				.put("invalid", "不替换")
				.build());
		}

		writer.flush(FileUtil.file("d:/test/templateResult.xlsx"), true);
	}
}
