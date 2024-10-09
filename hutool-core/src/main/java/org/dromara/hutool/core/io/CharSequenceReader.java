/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;

import java.io.Reader;
import java.util.Objects;

/**
 * 基于{@link CharSequence}的{@link Reader}实现，用于支持{@link CharSequence}的读取<br>
 * 相比jdk的StringReader非线程安全，速度更快。
 *
 * @author Looly
 * @since 6.0.0
 */
public class CharSequenceReader extends Reader {

	private final int start;
	private final int end;

	private final CharSequence str;
	/**
	 * 读取到的位置
	 */
	private int next;
	/**
	 * 读取标记位置
	 */
	private int mark;

	/**
	 * 构造
	 *
	 * @param str          {@link CharSequence}
	 * @param startInclude 开始位置（包含）
	 * @param endExclude   结束位置（不包含）
	 */
	public CharSequenceReader(CharSequence str, final int startInclude, final int endExclude) {
		Assert.isTrue(startInclude >= 0, "Start index is less than zero: {}", startInclude);
		Assert.isTrue(endExclude > startInclude, "End index is less than start {}: {}", startInclude, endExclude);

		if (null == str) {
			str = StrUtil.EMPTY;
		}
		this.str = str;
		final int length = str.length();
		this.start = Math.min(length, startInclude);
		this.end = Math.min(length, endExclude);

		this.next = startInclude;
		this.mark = startInclude;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public void mark(final int readAheadLimit) {
		mark = next;
	}

	@Override
	public void reset() {
		next = mark;
	}

	@Override
	public int read() {
		if (next >= end) {
			return IoUtil.EOF;
		}
		return str.charAt(next++);
	}

	@Override
	public int read(final char[] array, final int offset, final int length) {
		if (next >= end) {
			return IoUtil.EOF;
		}
		Objects.requireNonNull(array, "array");
		if (length < 0 || offset < 0 || offset + length > array.length) {
			throw new IndexOutOfBoundsException("Array Size=" + array.length +
				", offset=" + offset + ", length=" + length);
		}

		if (str instanceof String) {
			final int count = Math.min(length, end - next);
			((String) str).getChars(next, next + count, array, offset);
			next += count;
			return count;
		}
		if (str instanceof StringBuilder) {
			final int count = Math.min(length, end - next);
			((StringBuilder) str).getChars(next, next + count, array, offset);
			next += count;
			return count;
		}
		if (str instanceof StringBuffer) {
			final int count = Math.min(length, end - next);
			((StringBuffer) str).getChars(next, next + count, array, offset);
			next += count;
			return count;
		}

		int count = 0;
		for (int i = 0; i < length; i++) {
			final int c = read();
			if (c == IoUtil.EOF) {
				return count;
			}
			array[offset + i] = (char) c;
			count++;
		}
		return count;
	}

	@Override
	public long skip(final long n) {
		if (n < 0) {
			throw new IllegalArgumentException("Number of characters to skip is less than zero: " + n);
		}
		if (next >= end) {
			return 0;
		}
		final int dest = (int) Math.min(end, next + n);
		final int count = dest - next;
		next = dest;
		return count;
	}

	@Override
	public boolean ready() {
		return next < end;
	}

	@Override
	public void close() {
		next = start;
		mark = start;
	}

	@Override
	public String toString() {
		return str.subSequence(start, end).toString();
	}
}
