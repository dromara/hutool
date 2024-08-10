/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.cellwalk.CellHandler;
import org.apache.poi.ss.util.cellwalk.CellWalk;
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
}
