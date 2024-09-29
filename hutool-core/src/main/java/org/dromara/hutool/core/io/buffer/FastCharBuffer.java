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

package org.dromara.hutool.core.io.buffer;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;

/**
 * 代码移植自jetbrick<br>
 * 快速字符缓冲，将数据存放在缓冲集中，取代以往的单一数组
 *
 * @author jetbrick, looly
 */
public class FastCharBuffer extends FastBuffer implements CharSequence, Appendable {

	/**
	 * 缓冲集
	 */
	private char[][] buffers = new char[16][];
	/**
	 * 当前缓冲
	 */
	private char[] currentBuffer;

	/**
	 * 构造
	 */
	public FastCharBuffer() {
		this(IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 构造
	 *
	 * @param size 一个缓冲区的最小字节数
	 */
	public FastCharBuffer(final int size) {
		super(size);
	}

	/**
	 * 向快速缓冲加入数据
	 *
	 * @param array 数据
	 * @param off   偏移量
	 * @param len   字节数
	 * @return 快速缓冲自身 @see FastByteBuffer
	 */
	public FastCharBuffer append(final char[] array, final int off, final int len) {
		final int end = off + len;
		if ((off < 0) || (len < 0) || (end > array.length)) {
			throw new IndexOutOfBoundsException();
		}
		if (len == 0) {
			return this;
		}
		final int newSize = size + len;
		int remaining = len;

		if (currentBuffer != null) {
			// first try to fill current buffer
			final int part = Math.min(remaining, currentBuffer.length - offset);
			System.arraycopy(array, end - remaining, currentBuffer, offset, part);
			remaining -= part;
			offset += part;
			size += part;
		}

		if (remaining > 0) {
			// still some data left
			// ask for new buffer
			needNewBuffer(newSize);

			// then copy remaining
			// but this time we are sure that it will fit
			final int part = Math.min(remaining, currentBuffer.length - offset);
			System.arraycopy(array, end - remaining, currentBuffer, offset, part);
			offset += part;
			size += part;
		}

		return this;
	}

	/**
	 * 向快速缓冲加入数据
	 *
	 * @param array 数据
	 * @return 快速缓冲自身 @see FastByteBuffer
	 */
	public FastCharBuffer append(final char[] array) {
		return append(array, 0, array.length);
	}

	/**
	 * 向快速缓冲加入一个字节
	 *
	 * @param element 一个字节的数据
	 * @return 快速缓冲自身 @see FastByteBuffer
	 */
	public FastCharBuffer append(final char element) {
		if ((currentBuffer == null) || (offset == currentBuffer.length)) {
			needNewBuffer(size + 1);
		}

		currentBuffer[offset] = element;
		offset++;
		size++;

		return this;
	}

	/**
	 * 将另一个快速缓冲加入到自身
	 *
	 * @param buff 快速缓冲
	 * @return 快速缓冲自身 @see FastByteBuffer
	 */
	public FastCharBuffer append(final FastCharBuffer buff) {
		if (buff.size == 0) {
			return this;
		}
		for (int i = 0; i < buff.currentBufferIndex; i++) {
			append(buff.buffers[i]);
		}
		append(buff.currentBuffer, 0, buff.offset);
		return this;
	}

	/**
	 * 根据索引位返回缓冲集中的缓冲
	 *
	 * @param index 索引位
	 * @return 缓冲
	 */
	public char[] array(final int index) {
		return buffers[index];
	}

	@Override
	public void reset() {
		super.reset();
		currentBuffer = null;
	}

	/**
	 * 返回快速缓冲中的数据，如果缓冲区中的数据长度固定，则直接返回原始数组<br>
	 * 注意此方法共享数组，不能修改数组内容！
	 *
	 * @return 快速缓冲中的数据
	 */
	public char[] toArrayZeroCopyIfPossible() {
		if (1 == currentBufferIndex) {
			final int len = buffers[0].length;
			if (len == size) {
				return buffers[0];
			}
		}

		return toArray();
	}

	/**
	 * 返回快速缓冲中的数据
	 *
	 * @return 快速缓冲中的数据
	 */
	public char[] toArray() {
		return toArray(0, this.size);
	}

	/**
	 * 返回快速缓冲中的数据
	 *
	 * @param start 逻辑起始位置
	 * @param len   逻辑字节长
	 * @return 快速缓冲中的数据
	 */
	public char[] toArray(int start, int len) {
		Assert.isTrue(start >= 0, "Start must be greater than zero!");
		Assert.isTrue(len >= 0, "Length must be greater than zero!");

		if (start >= this.size || len == 0) {
			return new char[0];
		}
		if (len > (this.size - start)) {
			len = this.size - start;
		}
		int remaining = len;
		int pos = 0;
		final char[] result = new char[len];

		int i = 0;
		while (start >= buffers[i].length) {
			start -= buffers[i].length;
			i++;
		}

		while (i < buffersCount) {
			final char[] buf = buffers[i];
			final int bufLen = Math.min(buf.length - start, remaining);
			System.arraycopy(buf, start, result, pos, bufLen);
			pos += bufLen;
			remaining -= bufLen;
			if (remaining == 0) {
				break;
			}
			start = 0;
			i++;
		}
		return result;
	}

	/**
	 * 根据索引位返回一个字节
	 *
	 * @param index 索引位
	 * @return 一个字节
	 */
	public char get(int index) {
		if ((index >= size) || (index < 0)) {
			throw new IndexOutOfBoundsException();
		}
		int ndx = 0;
		while (true) {
			final char[] b = buffers[ndx];
			if (index < b.length) {
				return b[index];
			}
			ndx++;
			index -= b.length;
		}
	}

	@Override
	public String toString() {
		return new String(toArray());
	}

	@Override
	public char charAt(final int index) {
		return get(index);
	}

	@Override
	public CharSequence subSequence(final int start, final int end) {
		final int len = end - start;
		return new StringBuilder(len).append(toArray(start, len));
	}

	@Override
	public FastCharBuffer append(final CharSequence csq) {
		if(csq instanceof String){
			return append((String)csq);
		}
		return append(csq, 0, csq.length());
	}

	/**
	 * Appends character sequence to buffer.
	 */
	@Override
	public FastCharBuffer append(final CharSequence csq, final int start, final int end) {
		for (int i = start; i < end; i++) {
			append(csq.charAt(i));
		}
		return this;
	}

	/**
	 * 追加字符串
	 *
	 * @param string String
	 * @return this
	 */
	public FastCharBuffer append(final String string) {
		final int len = string.length();
		if (len == 0) {
			return this;
		}

		final int newSize = size + len;
		int remaining = len;
		int start = 0;

		if (currentBuffer != null) {
			// first try to fill current buffer
			final int part = Math.min(remaining, currentBuffer.length - offset);
			string.getChars(0, part, currentBuffer, offset);
			remaining -= part;
			offset += part;
			size += part;
			start += part;
		}

		if (remaining > 0) {
			// still some data left
			// ask for new buffer
			needNewBuffer(newSize);

			// then copy remaining
			// but this time we are sure that it will fit
			final int part = Math.min(remaining, currentBuffer.length - offset);
			string.getChars(start, start + part, currentBuffer, offset);
			offset += part;
			size += part;
		}

		return this;
	}

	@Override
	protected void needNewBuffer(final int newSize) {
		final int delta = newSize - size;
		final int newBufferSize = Math.max(minChunkLen, delta);

		currentBufferIndex++;
		currentBuffer = new char[newBufferSize];
		offset = 0;

		// add buffer
		if (currentBufferIndex >= buffers.length) {
			final int newLen = buffers.length << 1;
			final char[][] newBuffers = new char[newLen][];
			System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
			buffers = newBuffers;
		}
		buffers[currentBufferIndex] = currentBuffer;
		buffersCount++;
	}

}
