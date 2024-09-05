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

package org.dromara.hutool.poi.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.dromara.hutool.poi.excel.cell.values.NumericCellValue;
import org.dromara.hutool.poi.excel.reader.ExcelReader;
import org.dromara.hutool.poi.excel.writer.ExcelWriter;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class NumericCellValueTest {

	@Test
	public void writeTest() {
		final ExcelReader reader = ExcelUtil.getReader("1899bug_demo.xlsx");
		final ExcelWriter writer = ExcelUtil.getWriter("1899bug_write.xlsx");
		final Cell cell = reader.getCell(0, 0);
		// 直接取值
		// 和CellUtil.getCellValue(org.apache.poi.ss.usermodel.Cell)方法的结果一样
		// 1899-12-31 04:39:00
		final Date cellValue = cell.getDateCellValue();
		// 将这个值写入EXCEL中自定义样式的单元格，结果会是-1
		writer.writeCellValue(0, 0, cellValue);
		// 修改后的写入，单元格内容正常
		writer.writeCellValue(1, 0, new NumericCellValue(cell).getValue());
		writer.close();
		reader.close();
	}
}
