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
		final byte[] buf = new byte[bufferSize];
		int readChars = is.read(buf);
		if (readChars == -1) {
			// 空文件，返回0
			return 0;
		}

		// 起始行为1
		// 如果只有一行，无换行符，则读取结束后返回1
		// 如果多行，最后一行无换行符，最后一行需要单独计数
		// 如果多行，最后一行有换行符，则空行算作一行
		int count = 1;
		byte pre;
		byte c = 0;
		while (readChars == bufferSize) {
			for (int i = 0; i < bufferSize; i++) {
				pre = c;
				c = buf[i];
				// 换行符兼容MAC
				if (c == CharUtil.LF || pre == CharUtil.CR) {
					++count;
				}
			}
			readChars = is.read(buf);
		}

		// count remaining characters
		while (readChars != -1) {
			for (int i = 0; i < readChars; i++) {
				pre = c;
				c = buf[i];
				if (c == CharUtil.LF || pre == CharUtil.CR) {
					++count;
				}
			}
			readChars = is.read(buf);
		}

		// 最后一个字符为换行符，则单独计数行
		if(c == CharUtil.CR){
			++count;
		}

		return count;
	}
}
