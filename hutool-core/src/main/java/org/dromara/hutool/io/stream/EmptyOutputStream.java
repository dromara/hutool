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

package org.dromara.hutool.io.stream;

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
