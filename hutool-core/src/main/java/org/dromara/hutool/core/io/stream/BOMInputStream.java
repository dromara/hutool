/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.io.ByteOrderMark;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * 读取带BOM头的流内容，{@code getCharset()}方法调用后会得到BOM头的编码，且会去除BOM头<br>
 * BOM定义：<a href="http://www.unicode.org/unicode/faq/utf_bom.html">http://www.unicode.org/unicode/faq/utf_bom.html</a><br>
 * <ul>
 * <li>00 00 FE FF = UTF-32, big-endian</li>
 * <li>FF FE 00 00 = UTF-32, little-endian</li>
 * <li>EF BB BF = UTF-8</li>
 * <li>FE FF = UTF-16, big-endian</li>
 * <li>FF FE = UTF-16, little-endian</li>
 * </ul>
 * 使用： <br>
 * <code>
 * String enc = "UTF-8"; // or NULL to use systemdefault<br>
 * FileInputStream fis = new FileInputStream(file); <br>
 * BOMInputStream uin = new BOMInputStream(fis, enc); <br>
 * enc = uin.getCharset(); // check and skip possible BOM bytes
 * </code>
 * <br><br>
 * 参考： <a href="http://akini.mbnet.fi/java/unicodereader/UnicodeInputStream.java.txt">http://www.unicode.org/unicode/faq/utf_bom.html</a>
 *
 * @author looly
 */
public class BOMInputStream extends InputStream {

	private final PushbackInputStream in;
	private boolean isInited = false;
	private final String defaultCharset;
	private String charset;

	private static final int BOM_SIZE = 4;

	// ----------------------------------------------------------------- Constructor start

	/**
	 * 构造
	 * @param in 流
	 */
	public BOMInputStream(final InputStream in) {
		this(in, CharsetUtil.NAME_UTF_8);
	}

	/**
	 * 构造
	 *
	 * @param in 流
	 * @param defaultCharset 默认编码
	 */
	public BOMInputStream(final InputStream in, final String defaultCharset) {
		this.in = new PushbackInputStream(in, BOM_SIZE);
		this.defaultCharset = defaultCharset;
	}
	// ----------------------------------------------------------------- Constructor end

	/**
	 * 获取默认编码
	 *
	 * @return 默认编码
	 */
	public String getDefaultCharset() {
		return defaultCharset;
	}

	/**
	 * 获取BOM头中的编码
	 *
	 * @return 编码
	 */
	public String getCharset() {
		if (!isInited) {
			try {
				init();
			} catch (final IOException ex) {
				throw new IORuntimeException(ex);
			}
		}
		return charset;
	}

	@Override
	public void close() throws IOException {
		isInited = true;
		in.close();
	}

	@Override
	public int read() throws IOException {
		isInited = true;
		return in.read();
	}

	/**
	 * Read-ahead four bytes and check for BOM marks. <br>
	 * Extra bytes are unread back to the stream, only BOM bytes are skipped.
	 * @throws IOException 读取引起的异常
	 */
	protected void init() throws IOException {
		if (isInited) {
			return;
		}

		final byte[] bom = new byte[BOM_SIZE];
		final int n;
		int unread = 0;
		n = in.read(bom, 0, bom.length);

		for (final ByteOrderMark byteOrderMark : ByteOrderMark.ALL) {
			if(byteOrderMark.test(bom)){
				charset = byteOrderMark.getCharsetName();
				unread = n - byteOrderMark.length();
				break;
			}
		}
		 if(0 == unread) {
			// Unicode BOM mark not found, unread all bytes
			charset = defaultCharset;
			unread = n;
		}

		if (unread > 0) {
			in.unread(bom, (n - unread), unread);
		}

		isInited = true;
	}
}
