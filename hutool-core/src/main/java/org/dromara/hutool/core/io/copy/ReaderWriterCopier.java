/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io.copy;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.StreamProgress;
import org.dromara.hutool.core.lang.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * {@link Reader} 向 {@link Writer} 拷贝
 *
 * @author looly
 * @since 5.7.8
 */
public class ReaderWriterCopier extends IoCopier<Reader, Writer> {

	/**
	 * 构造
	 */
	public ReaderWriterCopier() {
		this(IoUtil.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小
	 */
	public ReaderWriterCopier(final int bufferSize) {
		this(bufferSize, -1);
	}

	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小
	 * @param count      拷贝总数
	 */
	public ReaderWriterCopier(final int bufferSize, final long count) {
		this(bufferSize, count, null);
	}

	/**
	 * 构造
	 *
	 * @param bufferSize 缓存大小
	 * @param count      拷贝总数
	 * @param progress   进度条
	 */
	public ReaderWriterCopier(final int bufferSize, final long count, final StreamProgress progress) {
		super(bufferSize, count, progress);
	}

	@Override
	public long copy(final Reader source, final Writer target) {
		Assert.notNull(source, "InputStream is null !");
		Assert.notNull(target, "OutputStream is null !");

		final StreamProgress progress = this.progress;
		if (null != progress) {
			progress.start();
		}
		final long size;
		try {
			size = doCopy(source, target, new char[bufferSize(this.count)], progress);
			target.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		if (null != progress) {
			progress.finish();
		}
		return size;
	}

	/**
	 * 执行拷贝，如果限制最大长度，则按照最大长度读取，否则一直读取直到遇到-1
	 *
	 * @param source   {@link InputStream}
	 * @param target   {@link OutputStream}
	 * @param buffer   缓存
	 * @param progress 进度条
	 * @return 拷贝总长度
	 * @throws IOException IO异常
	 */
	private long doCopy(final Reader source, final Writer target, final char[] buffer, final StreamProgress progress) throws IOException {
		long numToRead = this.count > 0 ? this.count : Long.MAX_VALUE;
		long total = 0;

		int read;
		while (numToRead > 0) {
			read = source.read(buffer, 0, bufferSize(numToRead));
			if (read < 0) {
				// 提前读取到末尾
				break;
			}
			target.write(buffer, 0, read);
			if(flushEveryBuffer){
				target.flush();
			}

			numToRead -= read;
			total += read;
			if (null != progress) {
				progress.progress(this.count, total);
			}
		}

		return total;
	}
}
