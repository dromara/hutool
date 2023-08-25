/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.poi.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Ignore;
import org.junit.Test;

public class IssueI6URF3Test {

	@Test
	@Ignore
	public void setCellStyleTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/issueI6URF3.xlsx");
		writer.writeCellValue(0, 0, 1);
		final CellStyle cellStyle = writer.createCellStyle(0, 0);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		writer.close();
	}
}
