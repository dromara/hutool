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

package org.dromara.hutool.poi.excel.cell.setters;

import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;

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
		super(escape(StrUtil.toStringOrNull(value)));
	}

	/**
	 * 使用 _x005F前缀转义_xXXXX_，避免被decode的问题<br>
	 * issue#I466ZZ@Gitee
	 *
	 * @param value 被转义的字符串
	 * @return 转义后的字符串
	 */
	private static String escape(final String value) {
		if (value == null || !value.contains("_x")) {
			return value;
		}

		// 使用 _x005F前缀转义_xXXXX_，避免被decode的问题
		// issue#I466ZZ@Gitee
		return ReUtil.replaceAll(value, utfPtrn, "_x005F$0");
	}
}
