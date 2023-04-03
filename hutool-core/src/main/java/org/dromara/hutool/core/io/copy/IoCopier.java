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

package org.dromara.hutool.core.io.copy;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.StreamProgress;

/**
 * IO拷贝抽象，可自定义包括缓存、进度条等信息<br>
 * 此对象非线程安全
 *
 * @param <S> 拷贝源类型，如InputStream、Reader等
 * @param <T> 拷贝目标类型，如OutputStream、Writer等
 * @author looly
 * @since 5.7.8
 */
public abstract class IoCopier<S, T> {

	protected final int bufferSize;
	/**
	 * 拷贝总数
	 */
	protected final long count;

	/**
	 * 进度条
	 */
	protected StreamProgress progress;

	/**
	 * 是否每次写出一个buffer内容就执行flush
	 */
	protected boolean flushEveryBuffer;

	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小，&lt; 0 表示默认{@link IoUtil#DEFAULT_BUFFER_SIZE}
	 * @param count      拷贝总数，-1表示无限制
	 * @param progress   进度条
	 */
	public IoCopier(final int bufferSize, final long count, final StreamProgress progress) {
		this.bufferSize = bufferSize > 0 ? bufferSize : IoUtil.DEFAULT_BUFFER_SIZE;
		this.count = count <= 0 ? Long.MAX_VALUE : count;
		this.progress = progress;
	}

	/**
	 * 执行拷贝
	 *
	 * @param source 拷贝源，如InputStream、Reader等
	 * @param target 拷贝目标，如OutputStream、Writer等
	 * @return 拷贝的实际长度
	 */
	public abstract long copy(S source, T target);

	/**
	 * 缓存大小，取默认缓存和目标长度最小值
	 *
	 * @param count 目标长度
	 * @return 缓存大小
	 */
	protected int bufferSize(final long count) {
		return (int) Math.min(this.bufferSize, count);
	}

	/**
	 * 设置是否每次写出一个buffer内容就执行flush
	 *
	 * @param flushEveryBuffer 是否每次写出一个buffer内容就执行flush
	 * @return this
	 * @since 5.7.18
	 */
	public IoCopier<S, T> setFlushEveryBuffer(final boolean flushEveryBuffer){
		this.flushEveryBuffer = flushEveryBuffer;
		return this;
	}
}
