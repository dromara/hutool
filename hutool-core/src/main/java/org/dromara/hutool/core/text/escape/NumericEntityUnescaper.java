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

package org.dromara.hutool.core.text.escape;

import org.dromara.hutool.core.text.replacer.StrReplacer;
import org.dromara.hutool.core.text.CharUtil;

/**
 * 形如&#39;的反转义器
 *
 * @author looly
 *
 */
public class NumericEntityUnescaper extends StrReplacer {
	private static final long serialVersionUID = 1L;

	@Override
	protected int replace(final CharSequence str, final int pos, final StringBuilder out) {
		final int len = str.length();
		// 检查以确保以&#开头
		if (str.charAt(pos) == '&' && pos < len - 2 && str.charAt(pos + 1) == '#') {
			int start = pos + 2;
			boolean isHex = false;
			final char firstChar = str.charAt(start);
			if (firstChar == 'x' || firstChar == 'X') {
				start++;
				isHex = true;
			}

			// 确保&#后还有数字
			if (start == len) {
				return 0;
			}

			int end = start;
			while (end < len && CharUtil.isHexChar(str.charAt(end))) {
				end++;
			}
			final boolean isSemiNext = (end != len) && (str.charAt(end) == ';');
			if (isSemiNext) {
				final int entityValue;
				try {
					if (isHex) {
						entityValue = Integer.parseInt(str.subSequence(start, end).toString(), 16);
					} else {
						entityValue = Integer.parseInt(str.subSequence(start, end).toString(), 10);
					}
				} catch (final NumberFormatException nfe) {
					return 0;
				}
				out.append((char)entityValue);
				return 2 + end - start + (isHex ? 1 : 0) + 1;
			}
		}
		return 0;
	}
}
