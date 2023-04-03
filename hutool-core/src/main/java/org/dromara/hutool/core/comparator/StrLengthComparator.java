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

package org.dromara.hutool.core.comparator;

import java.util.Comparator;

/**
 * 字符串长度比较器，短在前
 *
 * @author looly
 * @since 5.8.9
 */
public class StrLengthComparator implements Comparator<CharSequence> {
	/**
	 * 单例的字符串长度比较器，短在前
	 */
	public static final StrLengthComparator INSTANCE = new StrLengthComparator();

	@Override
	public int compare(final CharSequence o1, final CharSequence o2) {
		int result = Integer.compare(o1.length(), o2.length());
		if (0 == result) {
			result = CompareUtil.compare(o1.toString(), o2.toString());
		}
		return result;
	}
}
