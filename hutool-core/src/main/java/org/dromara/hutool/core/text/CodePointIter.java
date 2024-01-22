/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.text;

import java.util.Iterator;

/**
 * Unicode字符遍历器<br>
 * 参考：http://stackoverflow.com/a/21791059/6030888
 *
 * @author Looly
 */
public class CodePointIter implements Iterable<Integer> {

	final String str;

	/**
	 * 构造
	 *
	 * @param str 字符串
	 */
	public CodePointIter(final String str) {
		this.str = str;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {

			private final int length = str.length();
			private int nextIndex = 0;

			@Override
			public boolean hasNext() {
				return this.nextIndex < this.length;
			}

			@Override
			public Integer next() {
				final int result = str.codePointAt(this.nextIndex);
				this.nextIndex += Character.charCount(result);
				return result;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
