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

package cn.hutool.core.codec;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.text.split.SplitUtil;
import cn.hutool.core.util.CharUtil;

import java.util.List;

/**
 * Punycode是一个根据RFC 3492标准而制定的编码系统，主要用于把域名从地方语言所采用的Unicode编码转换成为可用于DNS系统的编码
 * <p>
 * 参考：<a href="https://blog.csdn.net/a19881029/article/details/18262671">https://blog.csdn.net/a19881029/article/details/18262671</a>
 *
 * @author looly
 * @since 5.5.2
 */
public class PunyCode {
	private static final int TMIN = 1;
	private static final int TMAX = 26;
	private static final int BASE = 36;
	private static final int INITIAL_N = 128;
	private static final int INITIAL_BIAS = 72;
	private static final int DAMP = 700;
	private static final int SKEW = 38;
	private static final char DELIMITER = '-';
	private static final String PUNY_CODE_PREFIX = "xn--";

	/**
	 * 将域名编码为PunyCode，会忽略"."的编码
	 *
	 * @param domain 域名
	 * @return 编码后的域名
	 * @throws UtilException 计算异常
	 */
	public static String encodeDomain(final String domain) throws UtilException {
		Assert.notNull(domain, "domain must not be null!");
		final List<String> split = SplitUtil.split(domain, StrUtil.DOT);
		final StringBuilder result = new StringBuilder(domain.length() * 4);
		for (final String str : split) {
			if (result.length() != 0) {
				result.append(CharUtil.DOT);
			}
			result.append(encode(str, true));
		}

		return result.toString();
	}

	/**
	 * 将内容编码为PunyCode
	 *
	 * @param input 字符串
	 * @return PunyCode字符串
	 * @throws UtilException 计算异常
	 */
	public static String encode(final CharSequence input) throws UtilException {
		return encode(input, false);
	}

	/**
	 * 将内容编码为PunyCode
	 *
	 * @param input      字符串
	 * @param withPrefix 是否包含 "xn--"前缀
	 * @return PunyCode字符串
	 * @throws UtilException 计算异常
	 */
	public static String encode(final CharSequence input, final boolean withPrefix) throws UtilException {
		Assert.notNull(input, "input must not be null!");
		int n = INITIAL_N;
		int delta = 0;
		int bias = INITIAL_BIAS;
		final int length = input.length();
		final StringBuilder output = new StringBuilder(length * 4);
		// Copy all basic code points to the output
		int b = 0;
		for (int i = 0; i < length; i++) {
			final char c = input.charAt(i);
			if (isBasic(c)) {
				output.append(c);
				b++;
			}
		}
		// Append delimiter
		if (b > 0) {
			if(b == length){
				// 无需要编码的字符
				return output.toString();
			}
			output.append(DELIMITER);
		}
		int h = b;
		while (h < length) {
			int m = Integer.MAX_VALUE;
			// Find the minimum code point >= n
			for (int i = 0; i < length; i++) {
				final char c = input.charAt(i);
				if (c >= n && c < m) {
					m = c;
				}
			}
			if (m - n > (Integer.MAX_VALUE - delta) / (h + 1)) {
				throw new UtilException("OVERFLOW");
			}
			delta = delta + (m - n) * (h + 1);
			n = m;
			for (int j = 0; j < length; j++) {
				final int c = input.charAt(j);
				if (c < n) {
					delta++;
					if (0 == delta) {
						throw new UtilException("OVERFLOW");
					}
				}
				if (c == n) {
					int q = delta;
					for (int k = BASE; ; k += BASE) {
						final int t;
						if (k <= bias) {
							t = TMIN;
						} else if (k >= bias + TMAX) {
							t = TMAX;
						} else {
							t = k - bias;
						}
						if (q < t) {
							break;
						}
						output.append((char) digit2codepoint(t + (q - t) % (BASE - t)));
						q = (q - t) / (BASE - t);
					}
					output.append((char) digit2codepoint(q));
					bias = adapt(delta, h + 1, h == b);
					delta = 0;
					h++;
				}
			}
			delta++;
			n++;
		}

		if (withPrefix) {
			output.insert(0, PUNY_CODE_PREFIX);
		}
		return output.toString();
	}

	/**
	 * 解码 PunyCode为域名
	 *
	 * @param domain 域名
	 * @return 解码后的域名
	 * @throws UtilException 计算异常
	 */
	public static String decodeDomain(final String domain) throws UtilException {
		Assert.notNull(domain, "domain must not be null!");
		final List<String> split = SplitUtil.split(domain, StrUtil.DOT);
		final StringBuilder result = new StringBuilder(domain.length() / 4 + 1);
		for (final String str : split) {
			if (result.length() != 0) {
				result.append(CharUtil.DOT);
			}
			result.append(StrUtil.startWithIgnoreEquals(str, PUNY_CODE_PREFIX) ? decode(str) : str);
		}

		return result.toString();
	}

	/**
	 * 解码 PunyCode为字符串
	 *
	 * @param input PunyCode
	 * @return 字符串
	 * @throws UtilException 计算异常
	 */
	public static String decode(String input) throws UtilException {
		Assert.notNull(input, "input must not be null!");
		input = StrUtil.removePrefixIgnoreCase(input, PUNY_CODE_PREFIX);

		int n = INITIAL_N;
		int i = 0;
		int bias = INITIAL_BIAS;
		final int length = input.length();
		final StringBuilder output = new StringBuilder(length / 4 + 1);
		int d = input.lastIndexOf(DELIMITER);
		if (d > 0) {
			for (int j = 0; j < d; j++) {
				final char c = input.charAt(j);
				if (isBasic(c)) {
					output.append(c);
				}
			}
			d++;
		} else {
			d = 0;
		}
		while (d < length) {
			final int oldi = i;
			int w = 1;
			for (int k = BASE; ; k += BASE) {
				if (d == length) {
					throw new UtilException("BAD_INPUT");
				}
				final int c = input.charAt(d++);
				final int digit = codepoint2digit(c);
				if (digit > (Integer.MAX_VALUE - i) / w) {
					throw new UtilException("OVERFLOW");
				}
				i = i + digit * w;
				final int t;
				if (k <= bias) {
					t = TMIN;
				} else if (k >= bias + TMAX) {
					t = TMAX;
				} else {
					t = k - bias;
				}
				if (digit < t) {
					break;
				}
				w = w * (BASE - t);
			}
			bias = adapt(i - oldi, output.length() + 1, oldi == 0);
			if (i / (output.length() + 1) > Integer.MAX_VALUE - n) {
				throw new UtilException("OVERFLOW");
			}
			n = n + i / (output.length() + 1);
			i = i % (output.length() + 1);
			output.insert(i, (char) n);
			i++;
		}

		return output.toString();
	}

	private static int adapt(int delta, final int numpoints, final boolean first) {
		if (first) {
			delta = delta / DAMP;
		} else {
			delta = delta / 2;
		}
		delta = delta + (delta / numpoints);
		int k = 0;
		while (delta > ((BASE - TMIN) * TMAX) / 2) {
			delta = delta / (BASE - TMIN);
			k = k + BASE;
		}
		return k + ((BASE - TMIN + 1) * delta) / (delta + SKEW);
	}

	private static boolean isBasic(final char c) {
		return c < 0x80;
	}

	/**
	 * 将数字转为字符，对应关系为：
	 * <pre>
	 *     0 -&gt; a
	 *     1 -&gt; b
	 *     ...
	 *     25 -&gt; z
	 *     26 -&gt; '0'
	 *     ...
	 *     35 -&gt; '9'
	 * </pre>
	 *
	 * @param d 输入字符
	 * @return 转换后的字符
	 * @throws UtilException 无效字符
	 */
	private static int digit2codepoint(final int d) throws UtilException {
		Assert.checkBetween(d, 0, 35);
		if (d < 26) {
			// 0..25 : 'a'..'z'
			return d + 'a';
		} else if (d < 36) {
			// 26..35 : '0'..'9';
			return d - 26 + '0';
		} else {
			throw new UtilException("BAD_INPUT");
		}
	}

	/**
	 * 将字符转为数字，对应关系为：
	 * <pre>
	 *     a -&gt; 0
	 *     b -&gt; 1
	 *     ...
	 *     z -&gt; 25
	 *     '0' -&gt; 26
	 *     ...
	 *     '9' -&gt; 35
	 * </pre>
	 *
	 * @param c 输入字符
	 * @return 转换后的字符
	 * @throws UtilException 无效字符
	 */
	private static int codepoint2digit(final int c) throws UtilException {
		if (c - '0' < 10) {
			// '0'..'9' : 26..35
			return c - '0' + 26;
		} else if (c - 'a' < 26) {
			// 'a'..'z' : 0..25
			return c - 'a';
		} else {
			throw new UtilException("BAD_INPUT");
		}
	}
}
