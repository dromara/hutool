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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.dromara.hutool.poi.excel.WorkbookUtil;
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
