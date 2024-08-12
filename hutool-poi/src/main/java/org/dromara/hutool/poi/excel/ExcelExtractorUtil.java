/*
 * Copyright (c) 2013-2024 Hutool Team.
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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * {@link ExcelExtractor}工具封装
 *
 * @author looly
 * @since 5.4.4
 */
public class ExcelExtractorUtil {
	/**
	 * 获取 {@link ExcelExtractor} 对象
	 *
	 * @param wb {@link Workbook}
	 * @return {@link ExcelExtractor}
	 */
	public static ExcelExtractor getExtractor(final Workbook wb) {
		final ExcelExtractor extractor;
		if (wb instanceof HSSFWorkbook) {
			extractor = new org.apache.poi.hssf.extractor.ExcelExtractor((HSSFWorkbook) wb);
		} else {
			extractor = new XSSFExcelExtractor((XSSFWorkbook) wb);
		}
		return extractor;
	}

	/**
	 * 读取为文本格式<br>
	 * 使用{@link ExcelExtractor} 提取Excel内容
	 *
	 * @param wb            {@link Workbook}
	 * @param withSheetName 是否附带sheet名
	 * @return Excel文本
	 * @since 4.1.0
	 */
	public static String readAsText(final Workbook wb, final boolean withSheetName) {
		final ExcelExtractor extractor = getExtractor(wb);
		extractor.setIncludeSheetNames(withSheetName);
		return extractor.getText();
	}
}
