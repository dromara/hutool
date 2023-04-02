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

package org.dromara.hutool.core.compress;

import org.dromara.hutool.core.io.IORuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.Inflater;

/**
 * {@link java.util.zip.InflaterInputStream}包装实现，实现"deflate"算法解压<br>
 * 参考：org.apache.hc.client5.http.entity.DeflateInputStream
 *
 * @author looly
 */
public class InflaterInputStream extends InputStream {

	private final java.util.zip.InflaterInputStream in;

	/**
	 * 构造
	 *
	 * @param wrapped 被包装的流
	 */
	public InflaterInputStream(final InputStream wrapped) {
		this(wrapped, 512);
	}

	/**
	 * 构造
	 *
	 * @param wrapped 被包装的流
	 * @param size    buffer大小
	 */
	public InflaterInputStream(final InputStream wrapped, final int size) {
		final PushbackInputStream pushback = new PushbackInputStream(wrapped, 2);
		final int i1, i2;
		try {
			i1 = pushback.read();
			i2 = pushback.read();
			if (i1 == -1 || i2 == -1) {
				throw new IORuntimeException("Unexpected end of stream");
			}

			pushback.unread(i2);
			pushback.unread(i1);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		boolean nowrap = true;
		final int b1 = i1 & 0xFF;
		final int compressionMethod = b1 & 0xF;
		final int compressionInfo = b1 >> 4 & 0xF;
		final int b2 = i2 & 0xFF;
		if (compressionMethod == 8 && compressionInfo <= 7 && ((b1 << 8) | b2) % 31 == 0) {
			nowrap = false;
		}
		in = new java.util.zip.InflaterInputStream(pushback, new Inflater(nowrap), size);
	}

	@Override
	public int read() throws IOException {
		return this.in.read();
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public int read(final byte[] b) throws IOException {
		return in.read(b);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		return in.read(b, off, len);
	}

	@Override
	public long skip(final long n) throws IOException {
		return in.skip(n);
	}

	@Override
	public int available() throws IOException {
		return in.available();
	}

	@Override
	public void mark(final int readLimit) {
		in.mark(readLimit);
	}

	@Override
	public void reset() throws IOException {
		in.reset();
	}

	@Override
	public boolean markSupported() {
		return in.markSupported();
	}

	@Override
	public void close() throws IOException {
		in.close();
	}
}
