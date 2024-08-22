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

import org.apache.poi.ss.util.CellReference;
import org.dromara.hutool.core.text.StrUtil;

/**
 * 单元格位置{@link CellReference}工具类，提供包括行号转行名称、列号转列名称等功能。
 *
 * @author looly
 * @since 6.0.0
 */
public class CellReferenceUtil {
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
	 * 例如：A11 -》 col:0,row:10，B5-》col:1,row:4
	 *
	 * @param locationRef 单元格地址标识符，例如A11，B5
	 * @return 坐标点
	 */
	public static CellReference toCellReference(final String locationRef) {
		return new CellReference(locationRef);
	}
}
