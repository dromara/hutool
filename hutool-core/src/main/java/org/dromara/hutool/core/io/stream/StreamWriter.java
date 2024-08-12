/*
 * Copyright (c) 2013-2024 Hutool Team.
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
import org.dromara.hutool.core.io.IoUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * {@link OutputStream}写出器
 *
 * @author looly
 */
public class StreamWriter {

	/**
	 * 创建写出器
	 *
	 * @param out             {@link OutputStream}
	 * @param closeAfterWrite 写出结束后是否关闭流
	 * @return StreamReader
	 */
	public static StreamWriter of(final OutputStream out, final boolean closeAfterWrite) {
		return new StreamWriter(out, closeAfterWrite);
	}

	private final OutputStream out;
	private final boolean closeAfterWrite;

	/**
	 * 构造
	 *
	 * @param out             {@link OutputStream}
	 * @param closeAfterWrite 写出结束后是否关闭流
	 */
	public StreamWriter(final OutputStream out, final boolean closeAfterWrite) {
		this.out = out;
		this.closeAfterWrite = closeAfterWrite;
	}

	/**
	 * 将byte[]写到流中
	 *
	 * @param content 写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public void write(final byte[] content) throws IORuntimeException {
		final OutputStream out = this.out;
		try {
			out.write(content);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (closeAfterWrite) {
				IoUtil.closeQuietly(out);
			}
		}
	}

	/**
	 * 将多部分对象写到流中，使用{@link ObjectOutputStream}，对象必须实现序列化接口
	 *
	 * @param contents 写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public void writeObjs(final Object... contents) throws IORuntimeException {
		ObjectOutputStream osw = null;
		try {
			osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
			for (final Object content : contents) {
				if (content != null) {
					osw.writeObject(content);
				}
			}
			osw.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (closeAfterWrite) {
				IoUtil.closeQuietly(osw);
			}
		}
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 *
	 * @param charset  写出的内容的字符集
	 * @param contents 写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 */
	public void writeStrs(final Charset charset, final CharSequence... contents) throws IORuntimeException {
		OutputStreamWriter osw = null;
		try {
			osw = IoUtil.toWriter(out, charset);
			for (final CharSequence content : contents) {
				if (content != null) {
					osw.write(content.toString());
				}
			}
			osw.flush();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (closeAfterWrite) {
				IoUtil.closeQuietly(osw);
			}
		}
	}
}
