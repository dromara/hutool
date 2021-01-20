package cn.hutool.core.codec;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * Punycode是一个根据RFC 3492标准而制定的编码系统，主要用于把域名从地方语言所采用的Unicode编码转换成为可用于DNS系统的编码
 * <p>
 * 参考：https://blog.csdn.net/a19881029/article/details/18262671
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

	public static final String PUNY_CODE_PREFIX = "xn--";

	/**
	 * 将内容编码为PunyCode
	 *
	 * @param input      字符串
	 * @return PunyCode字符串
	 * @throws UtilException 计算异常
	 */
	public static String encode(String input) throws UtilException {
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
	public static String encode(String input, boolean withPrefix) throws UtilException {
		int n = INITIAL_N;
		int delta = 0;
		int bias = INITIAL_BIAS;
		StringBuilder output = new StringBuilder();
		// Copy all basic code points to the output
		final int length = input.length();
		int b = 0;
		for (int i = 0; i < length; i++) {
			char c = input.charAt(i);
			if (isBasic(c)) {
				output.append(c);
				b++;
			}
		}
		// Append delimiter
		if (b > 0) {
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
				int c = input.charAt(j);
				if (c < n) {
					delta++;
					if (0 == delta) {
						throw new UtilException("OVERFLOW");
					}
				}
				if (c == n) {
					int q = delta;
					for (int k = BASE; ; k += BASE) {
						int t;
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

		if(withPrefix){
			output.insert(0, PUNY_CODE_PREFIX);
		}
		return output.toString();
	}

	/**
	 * 解码 PunyCode为字符串
	 *
	 * @param input PunyCode
	 * @return 字符串
	 * @throws UtilException 计算异常
	 */
	public static String decode(String input) throws UtilException {
		input = StrUtil.removePrefixIgnoreCase(input, PUNY_CODE_PREFIX);

		int n = INITIAL_N;
		int i = 0;
		int bias = INITIAL_BIAS;
		StringBuilder output = new StringBuilder();
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
		final int length = input.length();
		while (d < length) {
			int oldi = i;
			int w = 1;
			for (int k = BASE; ; k += BASE) {
				if (d == length) {
					throw new UtilException("BAD_INPUT");
				}
				int c = input.charAt(d++);
				int digit = codepoint2digit(c);
				if (digit > (Integer.MAX_VALUE - i) / w) {
					throw new UtilException("OVERFLOW");
				}
				i = i + digit * w;
				int t;
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

	private static int adapt(int delta, int numpoints, boolean first) {
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

	private static boolean isBasic(char c) {
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
	 * @param d 输入字符
	 * @return 转换后的字符
	 * @throws UtilException 无效字符
	 */
	private static int digit2codepoint(int d) throws UtilException {
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
	 * @param c 输入字符
	 * @return 转换后的字符
	 * @throws UtilException 无效字符
	 */
	private static int codepoint2digit(int c) throws UtilException {
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
