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
