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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI5Q1TWTest {

	@Test
	public void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("I5Q1TW.xlsx");

		// 自定义时间格式1
		Assertions.assertEquals("18:56", reader.readCellValue(0, 0).toString());

		// 自定义时间格式2
		Assertions.assertEquals("18:56", reader.readCellValue(1, 0).toString());
	}
}
