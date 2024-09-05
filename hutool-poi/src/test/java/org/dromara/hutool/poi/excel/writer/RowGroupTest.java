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

import org.apache.poi.ss.usermodel.*;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.dromara.hutool.poi.excel.RowGroup;
import org.dromara.hutool.poi.excel.style.StyleUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * https://blog.csdn.net/qq_45752401/article/details/121250993
 */
public class RowGroupTest {

	@Test
	@Disabled
	void writeSingleRowGroupTest() {
		final RowGroup rowGroup = RowGroup.of("分组表格测试");

		final ExcelWriter writer = ExcelUtil.getWriter();
		writer.writeHeader(rowGroup);
		writer.flush(FileUtil.file("d:/test/poi/singleRowGroup.xlsx"), true);
		writer.close();
	}

	@Test
	@Disabled
	void writeListRowGroupTest() {
		final RowGroup rowGroup = RowGroup.of(null)
			.addChild("标题1")
			.addChild("标题2")
			.addChild("标题3")
			.addChild("标题4");

		final ExcelWriter writer = ExcelUtil.getWriter();
		writer.writeHeader(rowGroup);
		writer.flush(FileUtil.file("d:/test/poi/listRowGroup.xlsx"), true);
		writer.close();
	}

	@Test
	@Disabled
	void writeOneRowGroupTest() {
		final RowGroup rowGroup = RowGroup.of("基本信息")
			.addChild(RowGroup.of("名称2"))
			.addChild(RowGroup.of("名称3"));

		final ExcelWriter writer = ExcelUtil.getWriter();
		writer.writeHeader(rowGroup);
		writer.flush(FileUtil.file("d:/test/poi/oneRowGroup.xlsx"), true);
		writer.close();
	}

	@Test
	@Disabled
	void writeOneRowGroupWithStyleTest() {
		final ExcelWriter writer = ExcelUtil.getWriter();
		final CellStyle cellStyle = writer.getWorkbook().createCellStyle();
		cellStyle.setWrapText(false);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		//设置标题内容字体
		final Font font = writer.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 15);
		font.setFontName("Arial");
		//设置边框样式
		StyleUtil.setBorder(cellStyle, BorderStyle.THICK, IndexedColors.RED);
		cellStyle.setFont(font);

		final RowGroup rowGroup = RowGroup.of("基本信息")
			.setStyle(cellStyle)
			.addChild(RowGroup.of("名称2"))
			.addChild(RowGroup.of("名称3"));

		writer.writeHeader(rowGroup);
		writer.flush(FileUtil.file("d:/test/poi/oneRowGroupWithStyle.xlsx"), true);
		writer.close();
	}

	@Test
	@Disabled
	void writeRowGroupTest() {
		final RowGroup rowGroup = RowGroup.of("分组表格测试")
			.addChild(RowGroup.of("序号"))
			.addChild(
				RowGroup.of("基本信息")
					.addChild(RowGroup.of("名称1"))
					.addChild(RowGroup.of("名称15")
						.addChild(RowGroup.of("名称2"))
						.addChild(RowGroup.of("名称3"))
					)
					.addChild(RowGroup.of("信息16")
						.addChild(RowGroup.of("名称4"))
						.addChild(RowGroup.of("名称5"))
						.addChild(RowGroup.of("名称6"))
					)
					.addChild(RowGroup.of("信息7"))
			)
			.addChild(RowGroup.of("特殊信息")
				.addChild(RowGroup.of("名称9"))
				.addChild(RowGroup.of("名称17")
					.addChild(RowGroup.of("名称10"))
					.addChild(RowGroup.of("名称11"))
				)
				.addChild(RowGroup.of("名称18")
					.addChild(RowGroup.of("名称12"))
					.addChild(RowGroup.of("名称13"))
				)
			)
			.addChild(RowGroup.of("名称14"));

		final ExcelWriter writer = ExcelUtil.getWriter();
		writer.writeHeader(rowGroup);
		writer.flush(FileUtil.file("d:/test/poi/rowGroup.xlsx"), true);
		writer.close();
	}

	@Test
	@Disabled
	void writeRowGroupTest2() {
		final RowGroup rowGroup = RowGroup.of("分组表格测试")
			.addChild(RowGroup.of("序号"))
			.addChild(
				RowGroup.of("基本信息")
					.addChild(RowGroup.of("名称1"))
					.addChild(RowGroup.of("名称15")
						.addChild(RowGroup.of("名称2"))
						.addChild(RowGroup.of("名称3")
							.addChild(RowGroup.of("名称3-1"))
							.addChild(RowGroup.of("名称3-2"))
						)
					)
					.addChild(RowGroup.of("信息16")
						.addChild(RowGroup.of("名称4"))
						.addChild(RowGroup.of("名称5"))
						.addChild(RowGroup.of("名称6"))
					)
					.addChild(RowGroup.of("信息7"))
			);

		final ExcelWriter writer = ExcelUtil.getWriter();
		writer.writeHeader(rowGroup);
		writer.flush(FileUtil.file("d:/test/poi/rowGroup2.xlsx"), true);
		writer.close();
	}
}
