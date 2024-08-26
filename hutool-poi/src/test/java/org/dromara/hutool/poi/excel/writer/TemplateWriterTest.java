package org.dromara.hutool.poi.excel.writer;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TemplateWriterTest {

	private static final String targetDir = "d:/test/templateWriter/";

	/**
	 * 正常数据填充
	 */
	@Test
	void writeTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("template.xlsx");
		writer.getConfig().setInsertRow(false);

		// 单个替换的变量
		writer.fillOnce(MapUtil
			.builder("date", (Object)"2024-01-01")
			.build());

		// 列表替换
		for (int i = 0; i < 10; i++) {
			writer.writeRow(createRow(), false);
		}

		writer.flush(FileUtil.file(targetDir + "templateResult.xlsx"), true);
		writer.close();
	}

	/**
	 * 带有页脚的数据填充，通过插入方式完成，页脚下移
	 */
	@Test
	void writeWithFooterTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("templateWithFooter.xlsx");
		//writer.getConfig().setInsertRow(true);

		// 单个替换的变量
		writer.fillOnce(MapUtil
			.builder("date", (Object)"2024-01-01")
			.build());

		// 列表替换
		for (int i = 0; i < 10; i++) {
			writer.writeRow(createRow(), false);
		}

		writer.flush(FileUtil.file(targetDir + "templateWithFooterResult.xlsx"), true);
		writer.close();
	}

	/**
	 * 错位数据，即变量不在一行上
	 */
	@Test
	void writeNoneOneLineTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("templateWithFooterNoneOneLine.xlsx");
		//writer.getConfig().setInsertRow(true);

		// 单个替换的变量
		writer.fillOnce(MapUtil
			.builder("date", (Object)"2024-01-01")
			.build());

		// 列表替换
		for (int i = 0; i < 10; i++) {
			writer.writeRow(createRow(), false);
		}

		writer.flush(FileUtil.file(targetDir + "templateWithFooterNoneOneLineResult.xlsx"), true);
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
