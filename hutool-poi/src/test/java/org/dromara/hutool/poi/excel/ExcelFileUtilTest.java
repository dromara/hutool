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
