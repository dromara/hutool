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

package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * CSV解析器，用于解析CSV文件
 *
 * @author looly
 * @since 5.8.0
 */
public class CsvTokener extends SimpleWrapper<Reader> implements Closeable {

	/**
	 * 在Reader的位置（解析到第几个字符）
	 */
	private long index;
	/**
	 * 前一个字符
	 */
	private int prev;
	/**
	 * 是否使用前一个字符
	 */
	private boolean usePrev;

	/**
	 * 构造
	 *
	 * @param reader {@link Reader}
	 */
	public CsvTokener(final Reader reader) {
		super(IoUtil.toBuffered(reader));
	}

	/**
	 * 读取下一个字符，并记录位置
	 *
	 * @return 下一个字符
	 */
	public int next() {
		if(this.usePrev){
			this.usePrev = false;
		}else{
			try {
				this.prev = this.raw.read();
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
		this.index++;
		return this.prev;
	}

	/**
	 * 将标记回退到第一个字符
	 *
	 * @throws IllegalStateException 当多次调用back时，抛出此异常
	 */
	public void back() throws IllegalStateException {
		if (this.usePrev || this.index <= 0) {
			throw new IllegalStateException("Stepping back two steps is not supported");
		}
		this.index --;
		this.usePrev = true;
	}

	/**
	 * 获取当前位置
	 *
	 * @return 位置
	 */
	public long getIndex() {
		return this.index;
	}

	@Override
	public void close() throws IOException {
		IoUtil.nullSafeClose(this.raw);
	}
}
