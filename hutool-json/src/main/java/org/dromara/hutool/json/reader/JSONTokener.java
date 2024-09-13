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

package org.dromara.hutool.json.reader;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.ReaderWrapper;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

/**
 * JSON解析器<br>
 * 用于解析JSON字符串，支持流式解析，即逐个字符解析，而不是一次性解析整个字符串。
 *
 * @author from JSON.org
 */
public class JSONTokener extends ReaderWrapper {

	/**
	 * JSON的分界符
	 */
	private static final String TOKENS = ",:]}/\\\"[{;=#";

	/**
	 * 定义结束（End of stream）为：0
	 */
	public static final int EOF = 0;

	private long character;
	/**
	 * 是否结尾 End of stream
	 */
	private boolean eof;
	/**
	 * 在Reader的位置（解析到第几个字符）
	 */
	private long index;
	/**
	 * 当前所在行
	 */
	private long line;
	/**
	 * 前一个字符
	 */
	private char previous;
	/**
	 * 是否使用前一个字符
	 */
	private boolean usePrevious;

	// ------------------------------------------------------------------------------------ Constructor start

	/**
	 * 从InputStream中构建，使用UTF-8编码
	 *
	 * @param inputStream InputStream
	 * @throws JSONException JSON异常，包装IO异常
	 */
	public JSONTokener(final InputStream inputStream) throws JSONException {
		this(IoUtil.toUtf8Reader(inputStream));
	}

	/**
	 * 从字符串中构建
	 *
	 * @param s JSON字符串
	 */
	public JSONTokener(final CharSequence s) {
		this(new StringReader(Assert.notBlank(s).toString()));
	}

	/**
	 * 从Reader中构建
	 *
	 * @param reader Reader
	 */
	public JSONTokener(final Reader reader) {
		super(IoUtil.toMarkSupport(Assert.notNull(reader)));
		this.eof = false;
		this.usePrevious = false;
		this.previous = 0;
		this.index = 0;
		this.character = 1;
		this.line = 1;
	}
	// ------------------------------------------------------------------------------------ Constructor end

	/**
	 * 将标记回退到第一个字符，重新开始解析新的JSON
	 *
	 * @throws JSONException JSON异常，包装IO异常
	 */
	public void back() throws JSONException {
		if (this.usePrevious || this.index <= 0) {
			throw new JSONException("Stepping back two steps is not supported");
		}
		this.index -= 1;
		this.character -= 1;
		this.usePrevious = true;
		this.eof = false;
	}

	/**
	 * @return 是否进入结尾
	 */
	public boolean end() {
		return this.eof && !this.usePrevious;
	}

	/**
	 * 检查是否到了结尾<br>
	 * 如果读取完毕后还有未读的字符，报错
	 */
	public void checkEnd(){
		if(EOF != nextClean()){
			throw syntaxError("Invalid JSON, Unread data after end.");
		}
	}

	/**
	 * 源字符串是否有更多的字符
	 *
	 * @return 如果未达到结尾返回true，否则false
	 * @throws JSONException JSON异常，包装IO异常
	 */
	public boolean more() throws JSONException {
		this.next();
		if (this.end()) {
			return false;
		}
		this.back();
		return true;
	}

	/**
	 * 获得源字符串中的下一个字符
	 *
	 * @return 下一个字符, or 0 if past the end of the source string.
	 * @throws JSONException JSON异常，包装IO异常
	 */
	public char next() throws JSONException {
		int c;
		if (this.usePrevious) {
			this.usePrevious = false;
			c = this.previous;
		} else {
			try {
				c = read();
			} catch (final IOException exception) {
				throw new JSONException(exception);
			}

			if (c <= EOF) { // End of stream
				this.eof = true;
				c = EOF;
			}
		}
		this.index += 1;
		if (this.previous == '\r') {
			this.line += 1;
			this.character = c == '\n' ? 0 : 1;
		} else if (c == '\n') {
			this.line += 1;
			this.character = 0;
		} else {
			this.character += 1;
		}
		this.previous = (char) c;
		return this.previous;
	}

	/**
	 * 获取上一个读取的字符，如果没有读取过则返回'\0'
	 *
	 * @return 上一个读取的字符
	 */
	protected char getPrevious() {
		return this.previous;
	}

	/**
	 * 获取16进制unicode转义符对应的字符值，如：
	 * <pre>{@code '4f60' -> '你'}</pre>
	 *
	 * @return 字符
	 */
	public char nextUnicode() {
		return (char) NumberUtil.parseInt(next(4), 16);
	}

	/**
	 * 获得接下来的n个字符
	 *
	 * @param n 字符数
	 * @return 获得的n个字符组成的字符串
	 * @throws JSONException 如果源中余下的字符数不足以提供所需的字符数，抛出此异常
	 */
	public char[] next(final int n) throws JSONException {
		final char[] chars = new char[n];

		int pos = 0;
		while (pos < n) {
			chars[pos] = this.next();
			if (this.end()) {
				throw this.syntaxError("Substring bounds error");
			}
			pos += 1;
		}
		return chars;
	}

	/**
	 * 获取下一个token字符
	 *
	 * @return token字符
	 * @throws JSONException 非Token字符
	 */
	public char nextTokenChar() throws JSONException {
		final char c = this.nextClean();
		if (isNotTokenChar(c)) {
			throw this.syntaxError("Invalid token char: " + c);
		}
		return c;
	}

	/**
	 * 获得下一个字符，跳过空白符
	 *
	 * @return 获得的字符，0表示没有更多的字符
	 * @throws JSONException 获得下一个字符时抛出的异常
	 */
	public char nextClean() throws JSONException {
		char c;
		while (true) {
			c = this.next();
			if (c == EOF || c > CharUtil.SPACE) {
				return c;
			}
		}
	}

	/**
	 * 读取下一个JSON中的key，支持不带引号的key
	 *
	 * @param c 当前字符
	 * @return 键字符串
	 * @throws JSONException 非引号包裹的字符串
	 */
	public String nextKey(final char c) throws JSONException {
		switch (c) {
			case JSONTokener.EOF:
				// 未关闭对象
				throw syntaxError("A JSONObject text must end with '}'");
			case CharUtil.DELIM_START:
			case CharUtil.BRACKET_START:
				if (CharUtil.DELIM_START == this.previous) {
					// 不允许嵌套对象，如{{}}或{[]}
					throw syntaxError("A JSONObject can not directly nest another JSONObject or JSONArray.");
				}
			case CharUtil.DOUBLE_QUOTES:
			case CharUtil.SINGLE_QUOTE:
				// 带有包装的key
				return nextWrapString(c);
			default:
				// 兼容不严格的JSON，如key不被引号包围的情况
				return nextUnwrapString(c);
		}
	}

	/**
	 * 获取下一个冒号，非冒号则抛出异常
	 *
	 * @throws JSONException 非冒号字符
	 * @return 冒号字符
	 */
	public char nextColon() throws JSONException {
		final char c = nextClean();
		if (c != CharUtil.COLON) {
			throw syntaxError("Expected a ':' after a key");
		}
		return c;
	}

	/**
	 * 读取一个字符串，包括：
	 * <ul>
	 *     <li>使用引号包裹的字符串，自动反转义。</li>
	 *     <li>无包装的字符串，不转义</li>
	 * </ul>
	 *
	 * @return 截止到引号前的字符串
	 * @throws JSONException 出现无结束的字符串时抛出此异常
	 */
	public String nextString() throws JSONException {
		final char c = nextClean();
		switch (c) {
			case CharUtil.DOUBLE_QUOTES:
			case CharUtil.SINGLE_QUOTE:
				return nextWrapString(c);
		}

		// 兼容不严格的JSON，如key不被双引号包围的情况
		return nextUnwrapString(c);
	}

	/**
	 * 获得下一个字符串，此字符串不以引号包围，不会处理转义符，主要解析：
	 * <ul>
	 *     <li>非严格的key（无引号包围的key）</li>
	 *     <li>boolean值的字符串表示</li>
	 *     <li>Number值的字符串表示</li>
	 *     <li>null的字符串表示</li>
	 * </ul>
	 *
	 * @param c 首个字符
	 * @return 字符串
	 * @throws JSONException 读取空串时抛出此异常
	 */
	public String nextUnwrapString(char c) throws JSONException {
		// 兼容不严格的JSON，如key不被双引号包围的情况
		final StringBuilder sb = new StringBuilder();
		while (isNotTokenChar(c)) {
			sb.append(c);
			c = next();
		}
		if (c != EOF) {
			back();
		}

		final String valueString = StrUtil.trim(sb);
		if (valueString.isEmpty()) {
			throw syntaxError("Missing value, maybe a token");
		}
		return valueString;
	}

	/**
	 * 返回当前位置到指定引号前的所有字符，反斜杠的转义符也会被处理。<br>
	 * 标准的JSON是不允许使用单引号包含字符串的，但是此实现允许。
	 *
	 * @param quote 字符引号, 包括 {@code "}（双引号） 或 {@code '}（单引号）。
	 * @return 截止到引号前的字符串
	 * @throws JSONException 出现无结束的字符串时抛出此异常
	 */
	public String nextWrapString(final char quote) throws JSONException {
		char c;
		final StringBuilder sb = new StringBuilder();
		while (true) {
			c = this.next();
			switch (c) {
				case EOF:
					throw this.syntaxError("Unterminated string");
				case CharUtil.LF:
				case CharUtil.CR:
					//throw this.syntaxError("Unterminated string");
					// https://gitee.com/dromara/hutool/issues/I76CSU
					// 兼容非转义符
					sb.append(c);
					break;
				case CharUtil.BACKSLASH:// 转义符
					c = this.next();
					sb.append(getUnescapeChar(c));
					break;
				default:
					// 字符串结束
					if (c == quote) {
						return sb.toString();
					}
					sb.append(c);
			}
		}
	}

	/**
	 * Make a JSONException to signal a syntax error. <br>
	 * 构建 JSONException 用于表示语法错误
	 *
	 * @param message 错误消息
	 * @return A JSONException object, suitable for throwing
	 */
	public JSONException syntaxError(final String message) {
		return new JSONException(message + this);
	}

	/**
	 * Make a printable string of this JSONTokener.
	 *
	 * @return " at {index} [character {character} line {line}]"
	 */
	@Override
	public String toString() {
		return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
	}

	/**
	 * 获取反转义的字符
	 *
	 * @param c 转义的字符，即`\`后的字符
	 * @return 反转义字符
	 */
	private char getUnescapeChar(final char c) {
		switch (c) {
			case 'b':
				return '\b';
			case 't':
				return '\t';
			case 'n':
				return '\n';
			case 'f':
				return '\f';
			case 'r':
				return '\r';
			case 'u':// Unicode符
				return nextUnicode();
			case CharUtil.DOUBLE_QUOTES:
			case CharUtil.SINGLE_QUOTE:
			case CharUtil.BACKSLASH:
			case CharUtil.SLASH:
				return c;
			default:
				throw this.syntaxError("Illegal escape.");
		}
	}

	/**
	 * 是否为可见的非Token字符，这些字符存在于JSON的非字符串value中。
	 *
	 * @param c char
	 * @return 是否为可见的非Token字符
	 */
	private static boolean isNotTokenChar(final char c) {
		return c >= ' ' && TOKENS.indexOf(c) < 0;
	}
}
