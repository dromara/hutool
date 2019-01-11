package cn.hutool.core.net;

import java.io.CharArrayWriter;
import java.nio.charset.Charset;
import java.util.BitSet;

import cn.hutool.core.util.CharUtil;

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
 * @author looly
 *
 */
public class URLEncoder {
	static BitSet dontNeedEncoding;
	static final int caseDiff = ('a' - 'A');
	static String dfltEncName = null;

	static {
		dontNeedEncoding = new BitSet(256);
		int i;
		for (i = 'a'; i <= 'z'; i++) {
			dontNeedEncoding.set(i);
		}
		for (i = 'A'; i <= 'Z'; i++) {
			dontNeedEncoding.set(i);
		}
		for (i = '0'; i <= '9'; i++) {
			dontNeedEncoding.set(i);
		}
		
		//空格单独处理
//		dontNeedEncoding.set(' '); 
		dontNeedEncoding.set('-');
		dontNeedEncoding.set('_');
		dontNeedEncoding.set('.');
		dontNeedEncoding.set('*');
	}
	
	/**
	 * 设置是否转码空格为%形式，false表示转为+，否则转为%20
	 * @param percentSpace
	 */
	public static void setPercentSpace(boolean percentSpace) {
		dontNeedEncoding.set(CharUtil.SPACE, (false ==percentSpace));
	}
	
	/**
	 * 编码
	 *
	 * @param 需要编码的字符串
	 * @param charset 编码
	 * @return 编码后的结果
	 */
	public static String encode(String s, Charset charset) {

		boolean needToChange = false;
		StringBuilder out = new StringBuilder(s.length());
		CharArrayWriter charArrayWriter = new CharArrayWriter();

		for (int i = 0; i < s.length();) {
			int c = (int) s.charAt(i);
			// System.out.println("Examining character: " + c);
			if (dontNeedEncoding.get(c)) {
				if (c == CharUtil.SPACE) {
					c = '+';
					needToChange = true;
				}
				out.append((char) c);
				i++;
			} else {
				// convert to external encoding before hex conversion
				do {
					charArrayWriter.write(c);
					/*
					 * If this character represents the start of a Unicode surrogate pair, then pass in two characters. It's not clear what should be done if a bytes reserved in the surrogate pairs
					 * range occurs outside of a legal surrogate pair. For now, just treat it as if it were any other character.
					 */
					if (c >= 0xD800 && c <= 0xDBFF) {
						/*
						 * System.out.println(Integer.toHexString(c) + " is high surrogate");
						 */
						if ((i + 1) < s.length()) {
							int d = (int) s.charAt(i + 1);
							/*
							 * System.out.println("\tExamining " + Integer.toHexString(d));
							 */
							if (d >= 0xDC00 && d <= 0xDFFF) {
								/*
								 * System.out.println("\t" + Integer.toHexString(d) + " is low surrogate");
								 */
								charArrayWriter.write(d);
								i++;
							}
						}
					}
					i++;
				} while (i < s.length() && !dontNeedEncoding.get((c = (int) s.charAt(i))));

				charArrayWriter.flush();
				String str = new String(charArrayWriter.toCharArray());
				byte[] ba = str.getBytes(charset);
				for (int j = 0; j < ba.length; j++) {
					out.append('%');
					char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
					// converting to use uppercase letter as part of
					// the hex value if ch is a letter.
					if (Character.isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
					ch = Character.forDigit(ba[j] & 0xF, 16);
					if (Character.isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
				}
				charArrayWriter.reset();
				needToChange = true;
			}
		}

		return (needToChange ? out.toString() : s);
	}
}
