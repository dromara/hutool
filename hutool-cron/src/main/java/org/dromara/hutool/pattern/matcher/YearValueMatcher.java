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

package org.dromara.hutool.pattern.matcher;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * 年匹配<br>
 * 考虑年数字太大，不适合boolean数组，单独使用{@link LinkedHashSet}匹配
 *
 * @author Looly
 */
public class YearValueMatcher implements PartMatcher {

	private final LinkedHashSet<Integer> valueList;

	public YearValueMatcher(final Collection<Integer> intValueList) {
		this.valueList = new LinkedHashSet<>(intValueList);
	}

	@Override
	public boolean test(final Integer t) {
		return valueList.contains(t);
	}

	@Override
	public int nextAfter(final int value) {
		for (final Integer year : valueList) {
			if (year >= value) {
				return year;
			}
		}

		// 年无效，此表达式整体无效
		return -1;
	}
}
