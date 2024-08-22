/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.cellwalk.CellHandler;
import org.apache.poi.ss.util.cellwalk.CellWalk;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * {@link Sheet}相关工具类
 *
 * @author Looly
 * @since 6.0.0
 */
public class SheetUtil {

	/**
	 * 获取或者创建sheet表<br>
	 * 如果sheet表在Workbook中已经存在，则获取之，否则创建之
	 *
	 * @param book      工作簿{@link Workbook}
	 * @param sheetName 工作表名
	 * @return 工作表{@link Sheet}
	 * @since 4.0.2
	 */
	public static Sheet getOrCreateSheet(final Workbook book, String sheetName) {
		if (null == book) {
			return null;
		}
		sheetName = StrUtil.isBlank(sheetName) ? "sheet1" : sheetName;
		Sheet sheet = book.getSheet(sheetName);
		if (null == sheet) {
			sheet = book.createSheet(sheetName);
		}
		return sheet;
	}

	/**
	 * 获取或者创建sheet表<br>
	 * 自定义需要读取或写出的Sheet，如果给定的sheet不存在，创建之（命名为默认）<br>
	 * 在读取中，此方法用于切换读取的sheet，在写出时，此方法用于新建或者切换sheet
	 *
	 * @param book       工作簿{@link Workbook}
	 * @param sheetIndex 工作表序号
	 * @return 工作表{@link Sheet}
	 * @since 5.2.1
	 */
	public static Sheet getOrCreateSheet(final Workbook book, final int sheetIndex) {
		Sheet sheet = null;
		try {
			sheet = book.getSheetAt(sheetIndex);
		} catch (final IllegalArgumentException ignore) {
			//ignore
		}
		if (null == sheet) {
			sheet = book.createSheet();
		}
		return sheet;
	}

	/**
	 * sheet是否为空
	 *
	 * @param sheet {@link Sheet}
	 * @return sheet是否为空
	 * @since 4.0.1
	 */
	public static boolean isEmpty(final Sheet sheet) {
		return null == sheet || (sheet.getLastRowNum() == 0 && sheet.getPhysicalNumberOfRows() == 0);
	}

	/**
	 * 遍历Sheet中的所有单元格
	 *
	 * @param sheet       {@link Sheet}
	 * @param cellHandler 单元格处理器
	 */
	public static void walk(final Sheet sheet, final CellHandler cellHandler) {
		walk(sheet,
			new CellRangeAddress(0, sheet.getLastRowNum(), 0, sheet.getLastRowNum()),
			cellHandler);
	}

	/**
	 * 遍历Sheet中的指定区域单元格
	 *
	 * @param sheet       {@link Sheet}
	 * @param range       区域
	 * @param cellHandler 单元格处理器
	 */
	public static void walk(final Sheet sheet, final CellRangeAddress range, final CellHandler cellHandler) {
		final CellWalk cellWalk = new CellWalk(sheet, range);
		cellWalk.traverse(cellHandler);
	}

	/**
	 * 设置忽略错误，即Excel中的绿色警告小标，只支持XSSFSheet和SXSSFSheet<br>
	 * 见：https://stackoverflow.com/questions/23488221/how-to-remove-warning-in-excel-using-apache-poi-in-java
	 *
	 * @param sheet             {@link Sheet}
	 * @param cellRangeAddress  指定单元格范围
	 * @param ignoredErrorTypes 忽略的错误类型列表
	 * @throws UnsupportedOperationException 如果sheet不是XSSFSheet
	 * @since 5.8.28
	 */
	public static void addIgnoredErrors(final Sheet sheet, final CellRangeAddress cellRangeAddress, final IgnoredErrorType... ignoredErrorTypes) throws UnsupportedOperationException {
		if (sheet instanceof XSSFSheet) {
			((XSSFSheet) sheet).addIgnoredErrors(cellRangeAddress, ignoredErrorTypes);
		} else if (sheet instanceof SXSSFSheet) {
			// SXSSFSheet并未提供忽略错误方法，获得其内部_sh字段设置
			final XSSFSheet xssfSheet = (XSSFSheet) FieldUtil.getFieldValue(sheet, "_sh");
			if (null != xssfSheet) {
				xssfSheet.addIgnoredErrors(cellRangeAddress, ignoredErrorTypes);
			}
		} else {
			throw new UnsupportedOperationException("Only XSSFSheet supports addIgnoredErrors");
		}
	}
}
