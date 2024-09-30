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

package org.dromara.hutool.core.io.buffer;

import org.dromara.hutool.core.io.IoUtil;

/**
 * 快速缓冲抽象类，用于快速读取、写入数据到缓冲区，减少内存复制<br>
 * 相对于普通Buffer，使用二维数组扩展长度，减少内存复制，提升性能
 *
 * @author Looly
 */
public abstract class FastBuffer {

	/**
	 * 一个缓冲区的最小字节数
	 */
	protected final int minChunkLen;

	/**
	 * 缓冲数
	 */
	protected int buffersCount;
	/**
	 * 当前缓冲索引
	 */
	protected int currentBufferIndex = -1;
	/**
	 * 当前缓冲偏移量
	 */
	protected int offset;
	/**
	 * 缓冲字节数
	 */
	protected int size;

	/**
	 * 构造
	 *
	 * @param size 一个缓冲区的最小字节数
	 */
	public FastBuffer(int size) {
		if (size <= 0) {
			size = IoUtil.DEFAULT_BUFFER_SIZE;
		}
		this.minChunkLen = Math.abs(size);
	}

	/**
	 * 当前缓冲位于缓冲区的索引位
	 *
	 * @return {@link #currentBufferIndex}
	 */
	public int index() {
		return currentBufferIndex;
	}

	/**
	 * 获取当前缓冲偏移量
	 *
	 * @return 当前缓冲偏移量
	 */
	public int offset() {
		return offset;
	}

	/**
	 * 复位缓冲
	 */
	public void reset() {
		size = 0;
		offset = 0;
		currentBufferIndex = -1;
		buffersCount = 0;
	}

	/**
	 * 获取缓冲总长度
	 *
	 * @return 缓冲总长度
	 */
	public int length() {
		return size;
	}

	/**
	 * 是否为空
	 *
	 * @return 是否为空
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 检查现有缓冲区是否满足capacity，不满足则分配新的区域分配下一个缓冲区，不会小于1024
	 *
	 * @param capacity 理想缓冲区字节数
	 */
	abstract protected void ensureCapacity(final int capacity);
}
