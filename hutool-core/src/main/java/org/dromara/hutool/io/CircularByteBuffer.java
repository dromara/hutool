/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.io;

import java.util.Objects;

/**
 * 循环缓冲区
 *
 * @author apache commons io
 */
public class CircularByteBuffer {
	private final byte[] buffer;
	private int startOffset;
	private int endOffset;
	private int currentNumberOfBytes;

	/**
	 * 默认缓冲大小的构造({@link IoUtil#DEFAULT_BUFFER_SIZE})
	 */
	public CircularByteBuffer() {
		this(IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 构造
	 *
	 * @param pSize 缓冲大小
	 */
	public CircularByteBuffer(final int pSize) {
		buffer = new byte[pSize];
		startOffset = 0;
		endOffset = 0;
		currentNumberOfBytes = 0;
	}

	/**
	 * 从buffer中读取下一个byte，同时移除这个bytes。
	 *
	 * @return The byte
	 * @throws IllegalStateException buffer为空抛出，使用{@link #hasBytes()},或 {@link #getCurrentNumberOfBytes()}判断
	 */
	public byte read() {
		if (currentNumberOfBytes <= 0) {
			throw new IllegalStateException("No bytes available.");
		}
		final byte b = buffer[startOffset];
		--currentNumberOfBytes;
		if (++startOffset == buffer.length) {
			startOffset = 0;
		}
		return b;
	}

	/**
	 * Returns the given number of bytes from the buffer by storing them in
	 * the given byte array at the given offset.
	 * 从buffer中获取指定长度的bytes，从给定的targetBuffer的targetOffset位置写出
	 *
	 * @param targetBuffer 目标bytes
	 * @param targetOffset 目标数组开始位置
	 * @param length       读取长度
	 * @throws NullPointerException     提供的数组为{@code null}
	 * @throws IllegalArgumentException {@code targetOffset}或{@code length} 为负数或{@code targetBuffer}太小
	 * @throws IllegalStateException    buffer中的byte不足，使用{@link #getCurrentNumberOfBytes()}判断。
	 */
	public void read(final byte[] targetBuffer, final int targetOffset, final int length) {
		Objects.requireNonNull(targetBuffer);
		if (targetOffset < 0 || targetOffset >= targetBuffer.length) {
			throw new IllegalArgumentException("Invalid offset: " + targetOffset);
		}
		if (length < 0 || length > buffer.length) {
			throw new IllegalArgumentException("Invalid length: " + length);
		}
		if (targetOffset + length > targetBuffer.length) {
			throw new IllegalArgumentException("The supplied byte array contains only "
					+ targetBuffer.length + " bytes, but offset, and length would require "
					+ (targetOffset + length - 1));
		}
		if (currentNumberOfBytes < length) {
			throw new IllegalStateException("Currently, there are only " + currentNumberOfBytes
					+ "in the buffer, not " + length);
		}
		int offset = targetOffset;
		for (int i = 0; i < length; i++) {
			targetBuffer[offset++] = buffer[startOffset];
			--currentNumberOfBytes;
			if (++startOffset == buffer.length) {
				startOffset = 0;
			}
		}
	}

	/**
	 * 增加byte到buffer中
	 *
	 * @param value The byte
	 * @throws IllegalStateException buffer已满. 用{@link #hasSpace()}或{@link #getSpace()}判断。
	 */
	public void add(final byte value) {
		if (currentNumberOfBytes >= buffer.length) {
			throw new IllegalStateException("No space available");
		}
		buffer[endOffset] = value;
		++currentNumberOfBytes;
		if (++endOffset == buffer.length) {
			endOffset = 0;
		}
	}

	/**
	 * Returns, whether the next bytes in the buffer are exactly those, given by
	 * {@code sourceBuffer}, {@code offset}, and {@code length}. No bytes are being
	 * removed from the buffer. If the result is true, then the following invocations
	 * of {@link #read()} are guaranteed to return exactly those bytes.
	 *
	 * @param sourceBuffer the buffer to compare against
	 * @param offset       start offset
	 * @param length       length to compare
	 * @return True, if the next invocations of {@link #read()} will return the
	 * bytes at offsets {@code pOffset}+0, {@code pOffset}+1, ...,
	 * {@code pOffset}+{@code pLength}-1 of byte array {@code pBuffer}.
	 * @throws IllegalArgumentException Either of {@code pOffset}, or {@code pLength} is negative.
	 * @throws NullPointerException     The byte array {@code pBuffer} is null.
	 */
	public boolean peek(final byte[] sourceBuffer, final int offset, final int length) {
		Objects.requireNonNull(sourceBuffer, "Buffer");
		if (offset < 0 || offset >= sourceBuffer.length) {
			throw new IllegalArgumentException("Invalid offset: " + offset);
		}
		if (length < 0 || length > buffer.length) {
			throw new IllegalArgumentException("Invalid length: " + length);
		}
		if (length < currentNumberOfBytes) {
			return false;
		}
		int localOffset = startOffset;
		for (int i = 0; i < length; i++) {
			if (buffer[localOffset] != sourceBuffer[i + offset]) {
				return false;
			}
			if (++localOffset == buffer.length) {
				localOffset = 0;
			}
		}
		return true;
	}

	/**
	 * Adds the given bytes to the buffer. This is the same as invoking {@link #add(byte)}
	 * for the bytes at offsets {@code offset+0}, {@code offset+1}, ...,
	 * {@code offset+length-1} of byte array {@code targetBuffer}.
	 *
	 * @param targetBuffer the buffer to copy
	 * @param offset       start offset
	 * @param length       length to copy
	 * @throws IllegalStateException    The buffer doesn't have sufficient space. Use
	 *                                  {@link #getSpace()} to prevent this exception.
	 * @throws IllegalArgumentException Either of {@code pOffset}, or {@code pLength} is negative.
	 * @throws NullPointerException     The byte array {@code pBuffer} is null.
	 */
	public void add(final byte[] targetBuffer, final int offset, final int length) {
		Objects.requireNonNull(targetBuffer, "Buffer");
		if (offset < 0 || offset >= targetBuffer.length) {
			throw new IllegalArgumentException("Invalid offset: " + offset);
		}
		if (length < 0) {
			throw new IllegalArgumentException("Invalid length: " + length);
		}
		if (currentNumberOfBytes + length > buffer.length) {
			throw new IllegalStateException("No space available");
		}
		for (int i = 0; i < length; i++) {
			buffer[endOffset] = targetBuffer[offset + i];
			if (++endOffset == buffer.length) {
				endOffset = 0;
			}
		}
		currentNumberOfBytes += length;
	}

	/**
	 * Returns, whether there is currently room for a single byte in the buffer.
	 * Same as {@link #hasSpace(int) hasSpace(1)}.
	 *
	 * @return true if there is space for a byte
	 * @see #hasSpace(int)
	 * @see #getSpace()
	 */
	public boolean hasSpace() {
		return currentNumberOfBytes < buffer.length;
	}

	/**
	 * Returns, whether there is currently room for the given number of bytes in the buffer.
	 *
	 * @param count the byte count
	 * @return true if there is space for the given number of bytes
	 * @see #hasSpace()
	 * @see #getSpace()
	 */
	public boolean hasSpace(final int count) {
		return currentNumberOfBytes + count <= buffer.length;
	}

	/**
	 * Returns, whether the buffer is currently holding, at least, a single byte.
	 *
	 * @return true if the buffer is not empty
	 */
	public boolean hasBytes() {
		return currentNumberOfBytes > 0;
	}

	/**
	 * Returns the number of bytes, that can currently be added to the buffer.
	 *
	 * @return the number of bytes that can be added
	 */
	public int getSpace() {
		return buffer.length - currentNumberOfBytes;
	}

	/**
	 * Returns the number of bytes, that are currently present in the buffer.
	 *
	 * @return the number of bytes
	 */
	public int getCurrentNumberOfBytes() {
		return currentNumberOfBytes;
	}

	/**
	 * Removes all bytes from the buffer.
	 */
	public void clear() {
		startOffset = 0;
		endOffset = 0;
		currentNumberOfBytes = 0;
	}
}
