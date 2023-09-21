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

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ExcelFileUtilTest {

	@Test
	public void xlsTest(){
		final InputStream in = FileUtil.getInputStream("aaa.xls");
		try{
			Assertions.assertTrue(ExcelFileUtil.isXls(in));
			Assertions.assertFalse(ExcelFileUtil.isXlsx(in));
		} finally {
			IoUtil.closeQuietly(in);
		}
	}

	@Test
	public void xlsxTest(){
		final InputStream in = FileUtil.getInputStream("aaa.xlsx");
		try{
			Assertions.assertFalse(ExcelFileUtil.isXls(in));
			Assertions.assertTrue(ExcelFileUtil.isXlsx(in));
		} finally {
			IoUtil.closeQuietly(in);
		}
	}
}
