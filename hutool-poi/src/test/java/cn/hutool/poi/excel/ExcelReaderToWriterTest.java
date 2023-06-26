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

import cn.hutool.core.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

public class ExcelReaderToWriterTest {

	@Test
	@Ignore
	public void getWriterTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/issue3120.xlsx");
		final ExcelWriter writer = reader.getWriter();
		writer.writeCellValue(0, 0, "替换内容222222");

		writer.flush(FileUtil.file("d:/test/2.xlsx"));
		writer.closeWithoutFlush();
	}
}
