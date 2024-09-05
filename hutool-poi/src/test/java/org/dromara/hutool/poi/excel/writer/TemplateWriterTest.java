/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.poi.excel.writer;

import lombok.AllArgsConstructor;
import lombok.Data;
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

	/**
	 * 错位数据，即变量不在一行上
	 */
	@Test
	void writeBeanTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("templateWithFooterNoneOneLine.xlsx");
		//writer.getConfig().setInsertRow(true);

		// 单个替换的变量
		writer.fillOnce(MapUtil
			.builder("date", (Object)"2024-01-01")
			.build());

		// 列表替换
		for (int i = 0; i < 10; i++) {
			writer.writeRow(new User("李四", 25, "某个街道"), false);
		}

		writer.flush(FileUtil.file(targetDir + "templateWriteWithBeanResult.xlsx"), true);
		writer.close();
	}

	private static Map<String, Object> createRow(){
		return MapUtil
			.builder("user.name", (Object)"张三")
			.put("user.age", 18)
			.put("year", 2024)
			.put("month", 8)
			.put("day", 24)
			.put("user.area123", "某某市")
			.put("invalid", "不替换")
			.build();
	}

	@Data
	@AllArgsConstructor
	private static class User{
		private String name;
		private int age;
		private String area123;
	}
}
