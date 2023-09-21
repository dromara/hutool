/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel;

import org.dromara.hutool.poi.excel.cell.values.NumericCellValue;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.Test;

public class NumericCellValueTest {

	@Test
	public void writeTest() {
		final ExcelReader reader = ExcelUtil.getReader("1899bug_demo.xlsx");
		final ExcelWriter writer = ExcelUtil.getWriter("1899bug_write.xlsx");
		final Cell cell = reader.getCell(0, 0);
		// 直接取值
		// 和CellUtil.getCellValue(org.mina.poi.ss.usermodel.Cell)方法的结果一样
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
