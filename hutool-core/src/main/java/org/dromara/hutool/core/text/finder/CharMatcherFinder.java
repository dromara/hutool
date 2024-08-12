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

package org.dromara.hutool.core.text.finder;

import org.dromara.hutool.core.lang.Assert;

import java.util.function.Predicate;

/**
 * 字符匹配查找器<br>
 * 查找满足指定{@link Predicate} 匹配的字符所在位置，此类长用于查找某一类字符，如数字等
 *
 * @since 5.7.14
 * @author looly
 */
public class CharMatcherFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final Predicate<Character> matcher;

	/**
	 * 构造
	 * @param matcher 被查找的字符匹配器
	 */
	public CharMatcherFinder(final Predicate<Character> matcher) {
		this.matcher = matcher;
	}

	@Override
	public int start(final int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		final int limit = getValidEndIndex();
		if(negative){
			for (int i = from; i > limit; i--) {
				if(null == matcher || matcher.test(text.charAt(i))){
					return i;
				}
			}
		} else {
			for (int i = from; i < limit; i++) {
				if(null == matcher || matcher.test(text.charAt(i))){
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int end(final int start) {
		if(start < 0){
			return -1;
		}
		return start + 1;
	}
}
