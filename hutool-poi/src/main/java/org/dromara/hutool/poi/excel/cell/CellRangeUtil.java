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

package org.dromara.hutool.poi.excel.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

/**
 * {@link CellRangeAddress} 工具类<br>
 * {@link CellRangeAddress} 表示表格的一个区域，通过定义起始行和列，以及结束行和列，圈定范围，如设置合并单元格等。
 *
 * @author Looly
 * @since 6.0.0
 */
public class CellRangeUtil {

	/**
	 * 根据字符串表示法创建一个CellRangeAddress对象。
	 * <p>
	 * 该方法提供了一种通过直接使用字符串引用来创建CellRangeAddress对象的便捷方式。
	 * 字符串引用应符合Excel中单元格范围的常规表示法，如"B1" 或 "A1:B2"。
	 *
	 * @param ref 代表单元格范围的字符串，格式为"A1:B2"。
	 * @return 根据给定字符串创建的CellRangeAddress对象。
	 * @see CellRangeAddress#valueOf(String)
	 */
	public static CellRangeAddress of(final String ref) {
		return CellRangeAddress.valueOf(ref);
	}

	/**
	 * 根据给定的行和列索引创建一个CellRangeAddress对象。
	 * 这个方法提供了一种简洁的方式，来表示一个Excel表格中特定的区域，由起始行和列以及结束行和列定义。
	 *
	 * @param firstRow 起始行的索引，从0开始计数。
	 * @param lastRow  结束行的索引，从0开始计数，必须大于firstRow。
	 * @param firstCol 起始列的索引，从0开始计数。
	 * @param lastCol  结束列的索引，从0开始计数，必须大于firstCol。
	 * @return 返回一个新的CellRangeAddress对象，表示由参数定义的区域。
	 */
	public static CellRangeAddress of(final int firstRow, final int lastRow, final int firstCol, final int lastCol) {
		return new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
	}

	/**
	 * 根据给定的行和列索引创建一个单行的CellRangeAddress对象，从首列开始。
	 * 这个方法提供了一种简洁的方式，来表示一个Excel表格中特定的区域，由起始行和列以及结束行和列定义。
	 *
	 * @param row      行的索引，从0开始计数。
	 * @param lastCol  结束列的索引，从0开始计数，必须大于firstCol。
	 * @return 返回一个新的CellRangeAddress对象，表示由参数定义的区域。
	 */
	public static CellRangeAddress ofSingleRow(final int row, final int lastCol) {
		return ofSingleRow(row, 0, lastCol);
	}

	/**
	 * 根据给定的行和列索引创建一个单行的CellRangeAddress对象。
	 * 这个方法提供了一种简洁的方式，来表示一个Excel表格中特定的区域，由起始行和列以及结束行和列定义。
	 *
	 * @param row      行的索引，从0开始计数。
	 * @param firstCol 起始列的索引，从0开始计数。
	 * @param lastCol  结束列的索引，从0开始计数，必须大于firstCol。
	 * @return 返回一个新的CellRangeAddress对象，表示由参数定义的区域。
	 */
	public static CellRangeAddress ofSingleRow(final int row, final int firstCol, final int lastCol) {
		return of(row, row, firstCol, lastCol);
	}

	/**
	 * 根据给定的行和列索引创建一个单列CellRangeAddress对象，从首行开始。
	 * 这个方法提供了一种简洁的方式，来表示一个Excel表格中特定的区域，由起始行和列以及结束行和列定义。
	 *
	 * @param lastRow  结束行的索引，从0开始计数，必须大于firstRow。
	 * @param col      列的索引，从0开始计数。
	 * @return 返回一个新的CellRangeAddress对象，表示由参数定义的区域。
	 */
	public static CellRangeAddress ofSingleColumn(final int lastRow, final int col) {
		return ofSingleColumn(0, lastRow, col);
	}

	/**
	 * 根据给定的行和列索引创建一个单列CellRangeAddress对象。
	 * 这个方法提供了一种简洁的方式，来表示一个Excel表格中特定的区域，由起始行和列以及结束行和列定义。
	 *
	 * @param firstRow 起始行的索引，从0开始计数。
	 * @param lastRow  结束行的索引，从0开始计数，必须大于firstRow。
	 * @param col      列的索引，从0开始计数。
	 * @return 返回一个新的CellRangeAddress对象，表示由参数定义的区域。
	 */
	public static CellRangeAddress ofSingleColumn(final int firstRow, final int lastRow, final int col) {
		return of(firstRow, lastRow, col, col);
	}

	// region ----- getCellRangeAddress
	/**
	 * 获取合并单元格{@link CellRangeAddress}，如果不是返回null
	 *
	 * @param sheet       {@link Sheet}
	 * @param locationRef 单元格地址标识符，例如A11，B5
	 * @return {@link CellRangeAddress}
	 * @since 5.8.0
	 */
	public static CellRangeAddress getCellRangeAddress(final Sheet sheet, final String locationRef) {
		final CellReference cellReference = new CellReference(locationRef);
		return getCellRangeAddress(sheet, cellReference.getCol(), cellReference.getRow());
	}

	/**
	 * 获取合并单元格{@link CellRangeAddress}，如果不是返回null
	 *
	 * @param cell {@link Cell}
	 * @return {@link CellRangeAddress}
	 * @since 5.8.0
	 */
	public static CellRangeAddress getCellRangeAddress(final Cell cell) {
		return getCellRangeAddress(cell.getSheet(), cell.getColumnIndex(), cell.getRowIndex());
	}

	/**
	 * 获取合并单元格{@link CellRangeAddress}，如果不是返回null
	 *
	 * @param sheet {@link Sheet}
	 * @param x     列号，从0开始
	 * @param y     行号，从0开始
	 * @return {@link CellRangeAddress}
	 * @since 5.8.0
	 */
	public static CellRangeAddress getCellRangeAddress(final Sheet sheet, final int x, final int y) {
		if (sheet != null) {
			final int sheetMergeCount = sheet.getNumMergedRegions();
			CellRangeAddress ca;
			for (int i = 0; i < sheetMergeCount; i++) {
				ca = sheet.getMergedRegion(i);
				if (y >= ca.getFirstRow() && y <= ca.getLastRow()
					&& x >= ca.getFirstColumn() && x <= ca.getLastColumn()) {
					return ca;
				}
			}
		}
		return null;
	}
	// endregion
}
