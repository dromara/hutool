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

package org.dromara.hutool.core.io.stream;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.text.CharUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * 行数计数器
 *
 * @since 6.0.0
 */
public class LineCounter implements Closeable {

	private final InputStream is;
	private final int bufferSize;

	private int count = -1;

	/**
	 * 构造
	 *
	 * @param is         输入流
	 * @param bufferSize 缓存大小，小于1则使用默认的1024
	 */
	public LineCounter(final InputStream is, final int bufferSize) {
		this.is = is;
		this.bufferSize = bufferSize < 1 ? 1024 : bufferSize;
	}

	/**
	 * 获取行数
	 *
	 * @return 行数
	 */
	public int getCount() {
		if (this.count < 0) {
			try {
				this.count = count();
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return this.count;
	}

	@Override
	public void close() throws IOException {
		if (null != this.is) {
			this.is.close();
		}
	}

	private int count() throws IOException {
		final byte[] c = new byte[bufferSize];
		int readChars = is.read(c);
		if (readChars == -1) {
			// 空文件，返回0
			return 0;
		}

		// 起始行为1
		// 如果只有一行，无换行符，则读取结束后返回1
		// 如果多行，最后一行无换行符，最后一行需要单独计数
		// 如果多行，最后一行有换行符，则空行算作一行
		int count = 1;
		while (readChars == bufferSize) {
			for (int i = 0; i < bufferSize; i++) {
				if (c[i] == CharUtil.LF) {
					++count;
				}
			}
			readChars = is.read(c);
		}

		// count remaining characters
		while (readChars != -1) {
			for (int i = 0; i < readChars; i++) {
				if (c[i] == CharUtil.LF) {
					++count;
				}
			}
			readChars = is.read(c);
		}

		return count;
	}
}
