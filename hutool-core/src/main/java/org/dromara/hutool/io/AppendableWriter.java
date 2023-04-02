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

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.nio.CharBuffer;

/**
 * 同时继承{@link Writer}和实现{@link Appendable}的聚合类，用于适配两种接口操作
 * 实现来自：jodd
 *
 * @author looly，jodd
 * @since 5.7.0
 */
public class AppendableWriter extends Writer implements Appendable {

	private final Appendable appendable;
	private final boolean flushable;
	private boolean closed;

	/**
	 * 构造
	 *
	 * @param appendable {@link Appendable}
	 */
	public AppendableWriter(final Appendable appendable) {
		this.appendable = appendable;
		this.flushable = appendable instanceof Flushable;
		this.closed = false;
	}

	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {
		checkNotClosed();
		appendable.append(CharBuffer.wrap(cbuf), off, off + len);
	}

	@Override
	public void write(final int c) throws IOException {
		checkNotClosed();
		appendable.append((char) c);
	}

	@Override
	public Writer append(final char c) throws IOException {
		checkNotClosed();
		appendable.append(c);
		return this;
	}

	@Override
	public Writer append(final CharSequence csq, final int start, final int end) throws IOException {
		checkNotClosed();
		appendable.append(csq, start, end);
		return this;
	}

	@Override
	public Writer append(final CharSequence csq) throws IOException {
		checkNotClosed();
		appendable.append(csq);
		return this;
	}

	@Override
	public void write(final String str, final int off, final int len) throws IOException {
		checkNotClosed();
		appendable.append(str, off, off + len);
	}

	@Override
	public void write(final String str) throws IOException {
		appendable.append(str);
	}

	@Override
	public void write(final char[] cbuf) throws IOException {
		appendable.append(CharBuffer.wrap(cbuf));
	}

	@Override
	public void flush() throws IOException {
		checkNotClosed();
		if (flushable) {
			((Flushable) appendable).flush();
		}
	}

	/**
	 * 检查Writer是否已经被关闭
	 *
	 * @throws IOException IO异常
	 */
	private void checkNotClosed() throws IOException {
		if (closed) {
			throw new IOException("Writer is closed!" + this);
		}
	}

	@Override
	public void close() throws IOException {
		if (false == closed) {
			flush();
			if (appendable instanceof Closeable) {
				((Closeable) appendable).close();
			}
			closed = true;
		}
	}
}
