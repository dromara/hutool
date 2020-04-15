package cn.hutool.core.net;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.HexUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.BitSet;

/**
 * URL编码，数据内容的类型是 application/x-www-form-urlencoded。
 * 
 * <pre>
 * 1.字符"a"-"z"，"A"-"Z"，"0"-"9"，"."，"-"，"*"，和"_" 都不会被编码;
 * 2.将空格转换为%20 ;
 * 3.将非文本内容转换成"%xy"的形式,xy是两位16进制的数值;
 * </pre>
 * 
 * @author looly,
 *
 */
public class URLEncoder implements Serializable{
	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------------------------------------------- Static method start
	/**
	 * 默认{@link URLEncoder}<br>
	 * 默认的编码器针对URI路径编码，定义如下：
	 * 
	 * <pre>
	 * pchar = unreserved（不处理） / pct-encoded / sub-delims（子分隔符） / ":" / "@"
	 * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * sub-delims = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
	 * </pre>
	 */
	public static final URLEncoder DEFAULT = createDefault();
	
	/**
	 * 用于查询语句的{@link URLEncoder}<br>
	 * 编码器针对URI路径编码，定义如下：
	 * 
	 * <pre>
	 * 0x20 ' ' =》 '+' 
	 * 0x2A, 0x2D, 0x2E, 0x30 to 0x39, 0x41 to 0x5A, 0x5F, 0x61 to 0x7A as-is 
	 * '*', '-', '.', '0' to '9', 'A' to 'Z', '_', 'a' to 'z' Also '=' and '&amp;' 不编码
	 * 其它编码为 %nn 形式
	 * </pre>
	 * 
	 * 详细见：https://www.w3.org/TR/html5/forms.html#application/x-www-form-urlencoded-encoding-algorithm
	 */
	public static final URLEncoder QUERY = createQuery();

	/**
	 * 创建默认{@link URLEncoder}<br>
	 * 默认的编码器针对URI路径编码，定义如下：
	 * 
	 * <pre>
	 * pchar = unreserved（不处理） / pct-encoded / sub-delims（子分隔符） / ":" / "@"
	 * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
	 * sub-delims = "!" / "$" / "&amp;" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
	 * </pre>
	 * 
	 * @return {@link URLEncoder}
	 */
	public static URLEncoder createDefault() {
		final URLEncoder encoder = new URLEncoder();
		encoder.addSafeCharacter('-');
		encoder.addSafeCharacter('.');
		encoder.addSafeCharacter('_');
		encoder.addSafeCharacter('~');
		// Add the sub-delims
		encoder.addSafeCharacter('!');
		encoder.addSafeCharacter('$');
		encoder.addSafeCharacter('&');
		encoder.addSafeCharacter('\'');
		encoder.addSafeCharacter('(');
		encoder.addSafeCharacter(')');
		encoder.addSafeCharacter('*');
		encoder.addSafeCharacter('+');
		encoder.addSafeCharacter(',');
		encoder.addSafeCharacter(';');
		encoder.addSafeCharacter('=');
		// Add the remaining literals
		encoder.addSafeCharacter(':');
		encoder.addSafeCharacter('@');
		// Add '/' so it isn't encoded when we encode a path
		encoder.addSafeCharacter('/');

		return encoder;
	}

	/**
	 * 创建用于查询语句的{@link URLEncoder}<br>
	 * 编码器针对URI路径编码，定义如下：
	 * 
	 * <pre>
	 * 0x20 ' ' =》 '+' 
	 * 0x2A, 0x2D, 0x2E, 0x30 to 0x39, 0x41 to 0x5A, 0x5F, 0x61 to 0x7A as-is 
	 * '*', '-', '.', '0' to '9', 'A' to 'Z', '_', 'a' to 'z' Also '=' and '&amp;' 不编码
	 * 其它编码为 %nn 形式
	 * </pre>
	 * 
	 * 详细见：https://www.w3.org/TR/html5/forms.html#application/x-www-form-urlencoded-encoding-algorithm
	 * 
	 * @return {@link URLEncoder}
	 */
	public static URLEncoder createQuery() {
		final URLEncoder encoder = new URLEncoder();
		// Special encoding for space
		encoder.setEncodeSpaceAsPlus(true);
		// Alpha and digit are safe by default
		// Add the other permitted characters
		encoder.addSafeCharacter('*');
		encoder.addSafeCharacter('-');
		encoder.addSafeCharacter('.');
		encoder.addSafeCharacter('_');
		encoder.addSafeCharacter('=');
		encoder.addSafeCharacter('&');

		return encoder;
	}
	// --------------------------------------------------------------------------------------------- Static method end

	/** 存放安全编码 */
	private final BitSet safeCharacters;
	/** 是否编码空格为+ */
	private boolean encodeSpaceAsPlus = false;

	/**
	 * 构造<br>
	 * 
	 * [a-zA-Z0-9]默认不被编码
	 */
	public URLEncoder() {
		this(new BitSet(256));

		for (char i = 'a'; i <= 'z'; i++) {
			addSafeCharacter(i);
		}
		for (char i = 'A'; i <= 'Z'; i++) {
			addSafeCharacter(i);
		}
		for (char i = '0'; i <= '9'; i++) {
			addSafeCharacter(i);
		}
	}

	/**
	 * 构造
	 * 
	 * @param safeCharacters 安全字符，安全字符不被编码
	 */
	private URLEncoder(BitSet safeCharacters) {
		this.safeCharacters = safeCharacters;
	}

	/**
	 * 增加安全字符<br>
	 * 安全字符不被编码
	 * 
	 * @param c 字符
	 */
	public void addSafeCharacter(char c) {
		safeCharacters.set(c);
	}

	/**
	 * 移除安全字符<br>
	 * 安全字符不被编码
	 * 
	 * @param c 字符
	 */
	public void removeSafeCharacter(char c) {
		safeCharacters.clear(c);
	}

	/**
	 * 是否将空格编码为+
	 * 
	 * @param encodeSpaceAsPlus 是否将空格编码为+
	 */
	public void setEncodeSpaceAsPlus(boolean encodeSpaceAsPlus) {
		this.encodeSpaceAsPlus = encodeSpaceAsPlus;
	}

	/**
	 * 将URL中的字符串编码为%形式
	 *
	 * @param path 需要编码的字符串
	 * @param charset 编码
	 *
	 * @return 编码后的字符串
	 */
	public String encode(String path, Charset charset) {
		final StringBuilder rewrittenPath = new StringBuilder(path.length());
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(buf, charset);

		int c;
		for (int i = 0; i < path.length(); i++) {
			c = path.charAt(i);
			if (safeCharacters.get(c)) {
				rewrittenPath.append((char) c);
			} else if (encodeSpaceAsPlus && c == CharUtil.SPACE) {
				// 对于空格单独处理
				rewrittenPath.append('+');
			} else {
				// convert to external encoding before hex conversion
				try {
					writer.write((char) c);
					writer.flush();
				} catch (IOException e) {
					buf.reset();
					continue;
				}

				byte[] ba = buf.toByteArray();
				for (byte toEncode : ba) {
					// Converting each byte in the buffer
					rewrittenPath.append('%');
					HexUtil.appendHex(rewrittenPath, toEncode, false);
				}
				buf.reset();
			}
		}
		return rewrittenPath.toString();
	}
}
