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
