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

package org.dromara.hutool.excel;

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
