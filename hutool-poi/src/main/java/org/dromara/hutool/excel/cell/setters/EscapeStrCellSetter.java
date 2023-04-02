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

package org.dromara.hutool.excel.cell.setters;

import org.dromara.hutool.regex.ReUtil;
import org.dromara.hutool.text.StrUtil;

import java.util.regex.Pattern;

/**
 * 字符串转义Cell值设置器<br>
 * 使用 _x005F前缀转义_xXXXX_，避免被decode的问题<br>
 * 如用户传入'_x5116_'会导致乱码，使用此设置器转义为'_x005F_x5116_'
 *
 * @author looly
 * @since 5.7.10
 */
public class EscapeStrCellSetter extends CharSequenceCellSetter {

	private static final Pattern utfPtrn = Pattern.compile("_x[0-9A-Fa-f]{4}_");

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	public EscapeStrCellSetter(final CharSequence value) {
		super(escape(StrUtil.str(value)));
	}

	/**
	 * 使用 _x005F前缀转义_xXXXX_，避免被decode的问题<br>
	 * issue#I466ZZ@Gitee
	 *
	 * @param value 被转义的字符串
	 * @return 转义后的字符串
	 */
	private static String escape(final String value) {
		if (value == null || false == value.contains("_x")) {
			return value;
		}

		// 使用 _x005F前缀转义_xXXXX_，避免被decode的问题
		// issue#I466ZZ@Gitee
		return ReUtil.replaceAll(value, utfPtrn, "_x005F$0");
	}
}
