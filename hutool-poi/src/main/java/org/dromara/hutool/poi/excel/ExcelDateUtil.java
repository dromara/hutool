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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.apache.poi.ss.formula.ConditionalFormattingEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ExcelNumberFormat;

/**
 * Excel中日期判断、读取、处理等补充工具类
 *
 * @author looly
 * @since 5.5.5
 */
public class ExcelDateUtil {

	/**
	 * 某些特殊的自定义日期格式
	 */
	private static final int[] customFormats = new int[]{28, 30, 31, 32, 33, 55, 56, 57, 58};

	/**
	 * 是否日期格式
	 *
	 * @param cell 单元格
	 * @return 是否日期格式
	 */
	public static boolean isDateFormat(final Cell cell) {
		return isDateFormat(cell, null);
	}

	/**
	 * 判断是否日期格式
	 *
	 * @param cell        单元格
	 * @param cfEvaluator {@link ConditionalFormattingEvaluator}
	 * @return 是否日期格式
	 */
	public static boolean isDateFormat(final Cell cell, final ConditionalFormattingEvaluator cfEvaluator) {
		final ExcelNumberFormat nf = ExcelNumberFormat.from(cell, cfEvaluator);
		return isDateFormat(nf);
	}

	/**
	 * 判断是否日期格式
	 *
	 * @param numFmt {@link ExcelNumberFormat}
	 * @return 是否日期格式
	 */
	public static boolean isDateFormat(final ExcelNumberFormat numFmt) {
		return isDateFormat(numFmt.getIdx(), numFmt.getFormat());
	}

	/**
	 * 判断日期格式
	 *
	 * @param formatIndex  格式索引，一般用于内建格式
	 * @param formatString 格式字符串
	 * @return 是否为日期格式
	 * @since 5.5.3
	 */
	public static boolean isDateFormat(final int formatIndex, final String formatString) {
		// issue#1283@Github
		if (ArrayUtil.contains(customFormats, formatIndex)) {
			return true;
		}

		// 自定义格式判断
		if (StrUtil.isNotEmpty(formatString) &&
				StrUtil.containsAny(formatString, "周", "星期", "aa")) {
			// aa  -> 周一
			// aaa -> 星期一
			return true;
		}

		return org.apache.poi.ss.usermodel.DateUtil.isADateFormat(formatIndex, formatString);
	}
}
