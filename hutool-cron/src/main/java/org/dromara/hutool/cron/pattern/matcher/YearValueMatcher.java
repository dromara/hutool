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

package org.dromara.hutool.cron.pattern.matcher;

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
