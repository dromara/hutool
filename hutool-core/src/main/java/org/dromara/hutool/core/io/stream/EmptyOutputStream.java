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

import java.io.OutputStream;

/**
 * 此OutputStream写出数据到<b>/dev/null</b>，即忽略所有数据<br>
 * 来自 Apache Commons io
 *
 * @author looly
 * @since 4.0.6
 */
public class EmptyOutputStream extends OutputStream {

	/**
	 * 单例
	 */
	public static final EmptyOutputStream INSTANCE = new EmptyOutputStream();

	private EmptyOutputStream() {
	}

	/**
	 * 什么也不做，写出到{@code /dev/null}.
	 *
	 * @param b   写出的数据
	 * @param off 开始位置
	 * @param len 长度
	 */
	@SuppressWarnings("NullableProblems")
	@Override
	public void write(final byte[] b, final int off, final int len) {
		// to /dev/null
	}

	/**
	 * 什么也不做，写出到 {@code /dev/null}.
	 *
	 * @param b 写出的数据
	 */
	@Override
	public void write(final int b) {
		// to /dev/null
	}

	/**
	 * 什么也不做，写出到 {@code /dev/null}.
	 *
	 * @param b 写出的数据
	 */
	@SuppressWarnings("NullableProblems")
	@Override
	public void write(final byte[] b) {
		// to /dev/null
	}

}
