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
import org.dromara.hutool.core.text.CharUtil;

/**
 * 字符查找器<br>
 * 查找指定字符在字符串中的位置信息
 *
 * @author looly
 * @since 5.7.14
 */
public class CharFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final char c;
	private final boolean caseInsensitive;

	/**
	 * 构造，不忽略字符大小写
	 *
	 * @param c 被查找的字符
	 */
	public CharFinder(final char c) {
		this(c, false);
	}

	/**
	 * 构造
	 *
	 * @param c               被查找的字符
	 * @param caseInsensitive 是否忽略大小写
	 */
	public CharFinder(final char c, final boolean caseInsensitive) {
		this.c = c;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public int start(final int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		final int limit = getValidEndIndex();
		if(negative){
			for (int i = from; i > limit; i--) {
				if (CharUtil.equals(c, text.charAt(i), caseInsensitive)) {
					return i;
				}
			}
		} else{
			for (int i = from; i < limit; i++) {
				if (CharUtil.equals(c, text.charAt(i), caseInsensitive)) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int end(final int start) {
		if (start < 0) {
			return -1;
		}
		return start + 1;
	}
}
