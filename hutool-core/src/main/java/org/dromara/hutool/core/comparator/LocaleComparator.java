/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.comparator;

import java.text.Collator;
import java.util.Locale;

/**
 * 自定义语言环境 String 比较，可为自然语言文本构建搜索和排序例程。
 *
 * @author looly
 */
public class LocaleComparator extends NullComparator<String> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param nullGreater   是否{@code null}在后
	 * @param desiredLocale 语言环境
	 */
	public LocaleComparator(final boolean nullGreater, final Locale desiredLocale) {
		super(nullGreater, Collator.getInstance(desiredLocale));
	}
}
