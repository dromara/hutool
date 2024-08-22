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

package org.dromara.hutool.core.text.finder;

import org.dromara.hutool.core.lang.Assert;

/**
 * 固定长度查找器<br>
 * 给定一个长度，查找的位置为from + length，一般用于分段截取
 *
 * @since 5.7.14
 * @author looly
 */
public class LengthFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final int length;

	/**
	 * 构造
	 * @param length 长度，必须大于0
	 */
	public LengthFinder(final int length) {
		Assert.isTrue(length > 0, "Length must be great than 0");
		this.length = length;
	}

	@Override
	public int start(final int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		final int limit = getValidEndIndex();
		final int result;
		if(negative){
			result = from - length;
			if(result > limit){
				return result;
			}
		} else {
			result = from + length;
			if(result < limit){
				return result;
			}
		}
		return -1;
	}

	@Override
	public int end(final int start) {
		return start;
	}
}
