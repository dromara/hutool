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

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.collection.iter.ComputeIter;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.CharUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 行读取器，类似于BufferedInputStream，支持多行转义，规则如下：<br>
 * <ul>
 *     <li>支持'\n'和'\r\n'两种换行符，不支持'\r'换行符</li>
 *     <li>如果想读取转义符，必须定义为'\\'</li>
 *     <li>多行转义后的换行符和空格都会被忽略</li>
 * </ul>
 * <p>
 * 例子：
 * <pre>
 * a=1\
 *   2
 * </pre>
 * 读出后就是{@code a=12}
 *
 * @author looly
 * @since 6.0.0
 */
public class LineReader extends ReaderWrapper implements Iterable<String> {

	/**
	 * 构造
	 *
	 * @param in      {@link InputStream}
	 * @param charset 编码
	 */
	public LineReader(final InputStream in, final Charset charset) {
		this(IoUtil.toReader(in, charset));
	}

	/**
	 * 构造
	 *
	 * @param reader {@link Reader}
	 */
	public LineReader(final Reader reader) {
		super(IoUtil.toBuffered(reader));
	}

	/**
	 * 读取一行
	 *
	 * @return 内容
	 * @throws IOException IO异常
	 */
	public String readLine() throws IOException {
		StringBuilder str = null;
		// 换行符前是否为转义符
		boolean precedingBackslash = false;
		int c;
		while ((c = read()) > 0) {
			if (null == str) {
				// 只有有字符的情况下才初始化行，否则为行结束
				str = StrUtil.builder(1024);
			}
			if (CharUtil.BACKSLASH == c) {
				// 转义符转义，行尾需要使用'\'时，使用转义符转义，即`\\`
				if (!precedingBackslash) {
					// 转义符，添加标识，但是不加入字符
					precedingBackslash = true;
					continue;
				} else {
					precedingBackslash = false;
				}
			} else {
				if (precedingBackslash) {
					// 转义模式下，跳过转义符后的所有空白符
					if (CharUtil.isBlankChar(c)) {
						continue;
					}
					// 遇到普通字符，关闭转义
					precedingBackslash = false;
				} else if (CharUtil.LF == c) {
					// 非转义状态下，表示行的结束
					// 如果换行符是`\r\n`，删除末尾的`\r`
					final int lastIndex = str.length() - 1;
					if (lastIndex >= 0 && CharUtil.CR == str.charAt(lastIndex)) {
						str.deleteCharAt(lastIndex);
					}
					break;
				}
			}

			str.append((char) c);
		}

		return StrUtil.toStringOrNull(str);
	}

	@Override
	public Iterator<String> iterator() {
		return new ComputeIter<String>() {
			@Override
			protected String computeNext() {
				try {
					return readLine();
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
			}
		};
	}
}
