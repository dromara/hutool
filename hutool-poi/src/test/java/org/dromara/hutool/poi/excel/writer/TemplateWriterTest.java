package org.dromara.hutool.poi.excel.writer;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TemplateWriterTest {

	@Test
	void insertTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/template.xlsx");
		writer.getConfig().setInsertRow(true);
		writer.setCurrentRow(3);
		writer.getSheet().shiftRows(4, 4, 10);

		writer.flush(FileUtil.file("d:/test/templateInsertResult.xlsx"), true);
		writer.close();
	}

	@Test
	void writeRowTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/template.xlsx");
		writer.getConfig().setInsertRow(true);

		// 单个替换的变量
		writer.fillOnce(MapUtil
			.builder("date", (Object)"2024-01-01")
			.build());

		// 列表替换
		for (int i = 0; i < 10; i++) {
			writer.writeRow(createRow(), false);
		}

		writer.flush(FileUtil.file("d:/test/templateResult.xlsx"), true);
		writer.close();
	}

	@Test
	void writeRowWithFooterTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/templateWithFooter.xlsx");
		writer.getConfig().setInsertRow(true);

		// 单个替换的变量
		writer.fillOnce(MapUtil
			.builder("date", (Object)"2024-01-01")
			.build());

		// 列表替换
		for (int i = 0; i < 10; i++) {
			writer.writeRow(createRow(), false);
		}

		writer.flush(FileUtil.file("d:/test/templateWithFooterResult.xlsx"), true);
		writer.close();
	}

	private static Map<String, Object> createRow(){
		return MapUtil
			.builder("user.name", (Object)"张三")
			.put("user.age", 18)
			.put("year", 2024)
			.put("month", 8)
			.put("day", 24)
			.put("day", 24)
			.put("user.area123", "某某市")
			.put("invalid", "不替换")
			.build();
	}
}
