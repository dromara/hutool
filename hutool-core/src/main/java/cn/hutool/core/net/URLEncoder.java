package cn.hutool.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.BitSet;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.HexUtil;

/**
 * URL编码，数据内容的类型是 application/x-www-form-urlencoded。
 * 
 * <pre>
 * 1.字符"a"-"z"，"A"-"Z"，"0"-"9"，"."，"-"，"*"，和"_" 都不会被编码;
 * 2.将空格转换为%20 ;
 * 3.将非文本内容转换成"%xy"的形式,xy是两位16进制的数值;
 * 4.在每个 name=value 对之间放置 & 符号。
 * </pre>
 * 
 * @author looly,
 *
 */
public final class URLEncoder {

	public static final URLEncoder DEFAULT = new URLEncoder();
	public static final URLEncoder QUERY = new URLEncoder();

	static {
		/*
		 * Encoder for URI paths, so from the spec:
		 *
		 * pchar = unreserved / pct-encoded / sub-delims / ":" / "@"
		 *
		 * unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
		 *
		 * sub-delims = "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="
		 */
		// ALPHA and DIGIT are always treated as safe characters
		// Add the remaining unreserved characters
		DEFAULT.addSafeCharacter('-');
		DEFAULT.addSafeCharacter('.');
		DEFAULT.addSafeCharacter('_');
		DEFAULT.addSafeCharacter('~');
		// Add the sub-delims
		DEFAULT.addSafeCharacter('!');
		DEFAULT.addSafeCharacter('$');
		DEFAULT.addSafeCharacter('&');
		DEFAULT.addSafeCharacter('\'');
		DEFAULT.addSafeCharacter('(');
		DEFAULT.addSafeCharacter(')');
		DEFAULT.addSafeCharacter('*');
		DEFAULT.addSafeCharacter('+');
		DEFAULT.addSafeCharacter(',');
		DEFAULT.addSafeCharacter(';');
		DEFAULT.addSafeCharacter('=');
		// Add the remaining literals
		DEFAULT.addSafeCharacter(':');
		DEFAULT.addSafeCharacter('@');
		// Add '/' so it isn't encoded when we encode a path
		DEFAULT.addSafeCharacter('/');

		/*
		 * Encoder for query strings https://www.w3.org/TR/html5/forms.html#application/x-www-form-urlencoded-encoding-algorithm 0x20 ' ' -> '+' 0x2A, 0x2D, 0x2E, 0x30 to 0x39, 0x41 to 0x5A, 0x5F,
		 * 0x61 to 0x7A as-is '*', '-', '.', '0' to '9', 'A' to 'Z', '_', 'a' to 'z' Also '=' and '&' are not encoded Everything else %nn encoded
		 */
		// Special encoding for space
		QUERY.setEncodeSpaceAsPlus(true);
		// Alpha and digit are safe by default
		// Add the other permitted characters
		QUERY.addSafeCharacter('*');
		QUERY.addSafeCharacter('-');
		QUERY.addSafeCharacter('.');
		QUERY.addSafeCharacter('_');
		QUERY.addSafeCharacter('=');
		QUERY.addSafeCharacter('&');
	}

	// Array containing the safe characters set.
	private final BitSet safeCharacters;

	private boolean encodeSpaceAsPlus = false;

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

	private URLEncoder(BitSet safeCharacters) {
		this.safeCharacters = safeCharacters;
	}

	public void addSafeCharacter(char c) {
		safeCharacters.set(c);
	}

	public void removeSafeCharacter(char c) {
		safeCharacters.clear(c);
	}

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

		int maxBytesPerChar = 10;
		final StringBuilder rewrittenPath = new StringBuilder(path.length());
		ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
		OutputStreamWriter writer = new OutputStreamWriter(buf, charset);

		int c;
		for (int i = 0; i < path.length(); i++) {
			c = path.charAt(i);
			if (safeCharacters.get(c)) {
				rewrittenPath.append((char) c);
			} else if (encodeSpaceAsPlus && c == CharUtil.SPACE) {
				//对于空格单独处理
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
				for (int j = 0; j < ba.length; j++) {
					// Converting each byte in the buffer
					byte toEncode = ba[j];
					rewrittenPath.append('%');
					HexUtil.appendHex(rewrittenPath, toEncode, false);
				}
				buf.reset();
			}
		}
		return rewrittenPath.toString();
	}
}
