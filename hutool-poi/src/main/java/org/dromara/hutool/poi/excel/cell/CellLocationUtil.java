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

package org.dromara.hutool.poi.excel.cell;

import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * 单元格位置工具类，提供包括行号转行名称、列号转列名称等功能。
 *
 * @author looly
 * @since 6.0.0
 */
public class CellLocationUtil {
	/**
	 * 将Sheet列号变为列名
	 *
	 * @param index 列号, 从0开始
	 * @return 0-》A; 1-》B...26-》AA
	 */
	public static String indexToColName(int index) {
		if (index < 0) {
			return null;
		}
		final StringBuilder colName = StrUtil.builder();
		do {
			if (colName.length() > 0) {
				index--;
			}
			final int remainder = index % 26;
			colName.append((char) (remainder + 'A'));
			index = (index - remainder) / 26;
		} while (index > 0);
		return colName.reverse().toString();
	}

	/**
	 * 根据表元的列名转换为列号
	 *
	 * @param colName 列名, 从A开始
	 * @return A1-》0; B1-》1...AA1-》26
	 */
	public static int colNameToIndex(final String colName) {
		final int length = colName.length();
		char c;
		int index = -1;
		for (int i = 0; i < length; i++) {
			c = Character.toUpperCase(colName.charAt(i));
			if (Character.isDigit(c)) {
				break;// 确定指定的char值是否为数字
			}
			index = (index + 1) * 26 + (int) c - 'A';
		}
		return index;
	}

	/**
	 * 将Excel中地址标识符（例如A11，B5）等转换为行列表示<br>
	 * 例如：A11 -》 x:0,y:10，B5-》x:1,y:4
	 *
	 * @param locationRef 单元格地址标识符，例如A11，B5
	 * @return 坐标点，x表示行，从0开始，y表示列，从0开始
	 */
	public static CellLocation toLocation(final String locationRef) {
		final int x = colNameToIndex(locationRef);
		final int y = ReUtil.getFirstNumber(locationRef) - 1;
		return new CellLocation(x, y);
	}
}
