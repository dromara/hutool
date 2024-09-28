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

package org.dromara.hutool.core.io.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * <p>This class is used to write a stream of chars as a stream of
 * bytes using the UTF8 encoding. It assumes that the underlying
 * output stream is buffered or does not need additional buffering.</p>
 *
 * <p>It is more efficient than using a {@link java.io.OutputStreamWriter}
 * because it does not need to be wrapped in a
 * {@link java.io.BufferedWriter}. Creating multiple instances
 * of {@link java.io.BufferedWriter} has been shown to be very expensive.</p>
 *
 * <p>copy from: {@code com.sun.xml.internal.stream.writers.UTF8OutputStreamWriter}</p>
 *
 * @author Santiago.PericasGeertsen@sun.com
 */
public class UTF8OutputStreamWriter extends Writer {

	/**
	 * Undelying output stream. This class assumes that this
	 * output stream does not need buffering.
	 */
	private final OutputStream out;

	/**
	 * Java represents chars that are not in the Basic Multilingual
	 * Plane (BMP) in UTF-16. This int stores the first code unit
	 * for a code point encoded in two UTF-16 code units.
	 */
	int lastUTF16CodePoint = 0;

	/**
	 * Creates a new UTF8OutputStreamWriter.
	 *
	 * @param out The underlying output stream.
	 */
	public UTF8OutputStreamWriter(final OutputStream out) {
		this.out = out;
	}

	@Override
	public void write(final int c) throws IOException {
		// Check in we are encoding at high and low surrogates
		if (lastUTF16CodePoint != 0) {
			final int uc =
				(((lastUTF16CodePoint & 0x3ff) << 10) | (c & 0x3ff)) + 0x10000;

			out.write(0xF0 | (uc >> 18));
			out.write(0x80 | ((uc >> 12) & 0x3F));
			out.write(0x80 | ((uc >> 6) & 0x3F));
			out.write(0x80 | (uc & 0x3F));

			lastUTF16CodePoint = 0;
			return;
		}

		// Otherwise, encode char as defined in UTF-8
		if (c < 0x80) {
			// 1 byte, 7 bits
			out.write((int) c);
		} else if (c < 0x800) {
			// 2 bytes, 11 bits
			out.write(0xC0 | (c >> 6));    // first 5
			out.write(0x80 | (c & 0x3F));  // second 6
		} else if (c <= '\uFFFF') {
			if (!isHighSurrogate(c) && !isLowSurrogate(c)) {
				// 3 bytes, 16 bits
				out.write(0xE0 | (c >> 12));   // first 4
				out.write(0x80 | ((c >> 6) & 0x3F));  // second 6
				out.write(0x80 | (c & 0x3F));  // third 6
			} else {
				lastUTF16CodePoint = c;
			}
		}
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	@Override
	public void write(final char[] cbuf) throws IOException {
		for (int i = 0; i < cbuf.length; i++) {
			write(cbuf[i]);
		}
	}

	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {
		for (int i = 0; i < len; i++) {
			write(cbuf[off + i]);
		}
	}

	@Override
	public void write(final String str) throws IOException {
		final int len = str.length();
		for (int i = 0; i < len; i++) {
			write(str.charAt(i));
		}
	}

	@Override
	public void write(final String str, final int off, final int len) throws IOException {
		for (int i = 0; i < len; i++) {
			write(str.charAt(off + i));
		}
	}

	@Override
	public void flush() throws IOException {
		out.flush();
	}

	@Override
	public void close() throws IOException {
		if (lastUTF16CodePoint != 0) {
			throw new IllegalStateException("Attempting to close a UTF8OutputStreamWriter"
				+ " while awaiting for a UTF-16 code unit");
		}
		out.close();
	}

	/**
	 * Returns whether the given character is a high surrogate
	 *
	 * @param c The character to check.
	 * @return true if the character is a high surrogate.
	 */
	private static boolean isHighSurrogate(final int c) {
		return (0xD800 <= c && c <= 0xDBFF);
	}

	/**
	 * Returns whether the given character is a low surrogate
	 *
	 * @param c The character to check.
	 * @return true if the character is a low surrogate.
	 */
	private static boolean isLowSurrogate(final int c) {
		return (0xDC00 <= c && c <= 0xDFFF);
	}
}
