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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * 通过一个 Writer和一个CharsetDecoder实现将字节数据输出为字符数据。可以通过不同的构造函数配置缓冲区大小和是否立即写入。
 * 来自：https://github.com/subchen/jetbrick-commons/blob/master/src/main/java/jetbrick/io/stream/WriterOutputStream.java
 *
 * @since 6.0.0
 * @author subchen
 */
public class WriterOutputStream extends OutputStream {
	private static final int DEFAULT_BUFFER_SIZE = IoUtil.DEFAULT_BUFFER_SIZE;

	private final Writer writer;
	private final CharsetDecoder decoder;
	private final boolean writeImmediately;
	private final ByteBuffer decoderIn;
	private final CharBuffer decoderOut;

	/**
	 * 构造函数，使用指定字符集和默认配置。
	 *
	 * @param writer 目标 Writer，用于写入字符数据
	 * @param charset 字符集，用于编码字节数据
	 */
	public WriterOutputStream(final Writer writer, final Charset charset) {
		this(writer, charset, DEFAULT_BUFFER_SIZE, false);
	}

	/**
	 * 构造函数，使用指定字符集、默认缓冲区大小和不立即写入配置。
	 *
	 * @param writer          目标 Writer，用于写入字符数据
	 * @param charset         字符集，用于编码字节数据
	 * @param bufferSize      缓冲区大小，用于控制字符数据的临时存储量
	 * @param writeImmediately 是否立即写入，如果为 true，则不使用内部缓冲区，每个字节立即被解码并写入
	 */
	public WriterOutputStream(final Writer writer, final Charset charset, final int bufferSize, final boolean writeImmediately) {
		this(writer, CharsetUtil.newDecoder(charset, CodingErrorAction.REPLACE), bufferSize, writeImmediately);
	}

	/**
	 * 构造，使用默认缓冲区大小和不立即写入配置。
	 *
	 * @param writer  目标 Writer，用于写入字符数据
	 * @param decoder 字符集解码器，用于将字节数据解码为字符数据
	 */
	public WriterOutputStream(final Writer writer, final CharsetDecoder decoder) {
		this(writer, decoder, DEFAULT_BUFFER_SIZE, false);
	}

	/**
	 * 构造，允许自定义缓冲区大小和是否立即写入的配置。
	 *
	 * @param writer           目标 Writer，用于写入字符数据
	 * @param decoder          字符集解码器，用于将字节数据解码为字符数据
	 * @param bufferSize       缓冲区大小，用于控制字符数据的临时存储量
	 * @param writeImmediately 是否立即写入，如果为 true，则不使用内部缓冲区，每个字节立即被解码并写入
	 */
	public WriterOutputStream(final Writer writer, final CharsetDecoder decoder, final int bufferSize, final boolean writeImmediately) {
		this.writer = writer;
		this.decoder = decoder;
		this.writeImmediately = writeImmediately;
		this.decoderOut = CharBuffer.allocate(bufferSize);
		this.decoderIn = ByteBuffer.allocate(128);
	}

	@Override
	public void write(final byte[] b, int off, int len) throws IOException {
		while (len > 0) {
			final int c = Math.min(len, decoderIn.remaining());
			decoderIn.put(b, off, c);
			processInput(false);
			len -= c;
			off += c;
		}
		if (writeImmediately) flushOutput();
	}

	@Override
	public void write(final byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	@Override
	public void write(final int b) throws IOException {
		write(new byte[]{(byte) b}, 0, 1);
	}

	@Override
	public void flush() throws IOException {
		flushOutput();
		writer.flush();
	}

	@Override
	public void close() throws IOException {
		processInput(true);
		flushOutput();
		writer.close();
	}

	private void processInput(final boolean endOfInput) throws IOException {
		decoderIn.flip();
		CoderResult coderResult;
		while (true) {
			coderResult = decoder.decode(decoderIn, decoderOut, endOfInput);
			if (!coderResult.isOverflow()) break;
			flushOutput();
		}
		if (!coderResult.isUnderflow()) {
			throw new IOException("Unexpected coder result");
		}

		decoderIn.compact();
	}

	private void flushOutput() throws IOException {
		if (decoderOut.position() > 0) {
			writer.write(decoderOut.array(), 0, decoderOut.position());
			decoderOut.rewind();
		}
	}
}
