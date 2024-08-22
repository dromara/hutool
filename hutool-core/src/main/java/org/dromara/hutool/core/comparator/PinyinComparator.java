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

package org.dromara.hutool.core.comparator;

import java.util.Locale;

/**
 * 按照GBK拼音顺序对给定的汉字字符串排序
 *
 * @author looly
 * @since 4.0.8
 */
public class PinyinComparator extends LocaleComparator {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造，{@code null}最大，排在最后
	 */
	public PinyinComparator() {
		this(true);
	}

	/**
	 * 构造
	 *
	 * @param nullGreater 是否{@code null}最大，排在最后
	 */
	public PinyinComparator(final boolean nullGreater) {
		super(nullGreater, Locale.CHINESE);
	}
}
