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
 * 代码移植自<a href="https://github.com/biezhi/blade">blade</a><br>
 * 快速缓冲，将数据存放在缓冲集中，取代以往的单一数组
 *
 * @author biezhi, looly
 * @since 1.0
 */
public class FastByteBuffer extends FastBuffer {

	/**
	 * 缓冲集
	 */
	private byte[][] buffers = new byte[16][];
	/**
	 * 当前缓冲
	 */
	private byte[] currentBuffer;

	/**
	 * 构造
	 */
	public FastByteBuffer() {
		this(IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 构造
	 *
	 * @param size 一个缓冲区的最小字节数
	 */
	public FastByteBuffer(final int size) {
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
	public FastByteBuffer append(final byte[] array, final int off, final int len) {
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
			ensureCapacity(newSize);

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
	public FastByteBuffer append(final byte[] array) {
		return append(array, 0, array.length);
	}

	/**
	 * 向快速缓冲加入一个字节
	 *
	 * @param element 一个字节的数据
	 * @return 快速缓冲自身 @see FastByteBuffer
	 */
	public FastByteBuffer append(final byte element) {
		if ((currentBuffer == null) || (offset == currentBuffer.length)) {
			ensureCapacity(size + 1);
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
	public FastByteBuffer append(final FastByteBuffer buff) {
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
	public byte[] array(final int index) {
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
	public byte[] toArrayZeroCopyIfPossible() {
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
	public byte[] toArray() {
		return toArray(0, this.size);
	}

	/**
	 * 返回快速缓冲中的数据
	 *
	 * @param start 逻辑起始位置
	 * @param len   逻辑字节长
	 * @return 快速缓冲中的数据
	 */
	public byte[] toArray(int start, int len) {
		Assert.isTrue(start >= 0, "Start must be greater than zero!");
		Assert.isTrue(len >= 0, "Length must be greater than zero!");

		if (start >= this.size || len == 0) {
			return new byte[0];
		}
		if (len > (this.size - start)) {
			len = this.size - start;
		}
		int remaining = len;
		int pos = 0;
		final byte[] result = new byte[len];

		int i = 0;
		while (start >= buffers[i].length) {
			start -= buffers[i].length;
			i++;
		}

		while (i < buffersCount) {
			final byte[] buf = buffers[i];
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
	public byte get(int index) {
		if ((index >= size) || (index < 0)) {
			throw new IndexOutOfBoundsException();
		}
		int ndx = 0;
		while (true) {
			final byte[] b = buffers[ndx];
			if (index < b.length) {
				return b[index];
			}
			ndx++;
			index -= b.length;
		}
	}

	@Override
	protected void ensureCapacity(final int capacity) {
		final int delta = capacity - size;
		final int newBufferSize = Math.max(minChunkLen, delta);

		currentBufferIndex++;
		currentBuffer = new byte[newBufferSize];
		offset = 0;

		// add buffer
		if (currentBufferIndex >= buffers.length) {
			final int newLen = buffers.length << 1;
			final byte[][] newBuffers = new byte[newLen][];
			System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
			buffers = newBuffers;
		}
		buffers[currentBufferIndex] = currentBuffer;
		buffersCount++;
	}
}
