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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.poi.excel.cell.CellUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * https://gitee.com/dromara/hutool/issues/I6MBS5<br>
 * 经过测试，发现BigExcelWriter中的comment会错位<br>
 * 修正方式见: https://stackoverflow.com/questions/28169011/using-sxssfapache-poi-and-adding-comment-does-not-generate-proper-excel-file
 */
public class IssueI6MBS5Test {

	@Test
	@Disabled
	public void setCommentTest() {
		final ExcelWriter writer = ExcelUtil.getBigWriter("d:/test/setCommentTest.xlsx");
		final Cell cell = writer.getOrCreateCell(0, 0);
		CellUtil.setCellValue(cell, "cellValue");
		CellUtil.setComment(cell, "commonText", "ascend");

		writer.close();
	}

	@Test
	@Disabled
	public void setCommentTest2() {
		final File file = new File("D:\\test\\CellUtilTest.xlsx");
		try (final Workbook workbook = WorkbookUtil.createBook(true)) {
			final Sheet sheet = workbook.createSheet();
			final Row row = sheet.createRow(0);
			final Cell cell = row.createCell(0);
			CellUtil.setCellValue(cell, "cellValue");
			CellUtil.setComment(cell, "commonText", "ascend", null);
			workbook.write(Files.newOutputStream(file.toPath()));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
