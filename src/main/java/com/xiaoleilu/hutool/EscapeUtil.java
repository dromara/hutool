package com.xiaoleilu.hutool;

/**
 * Escape / Unescape
 * @author xiaoleilu
 */
public class EscapeUtil {
	/**
	 * Escape编码（Unicode）
	 * @param content
	 * @return 编码后的字符串
	 */
	public static String escape(String content) {
		if(StrUtil.isBlank(content)) {
			return content;
		}
		
		int i;
		char j;
		StringBuilder tmp = new StringBuilder();
		tmp.ensureCapacity(content.length() * 6);

		for (i = 0; i < content.length(); i++) {

			j = content.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16) tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * Escape解码
	 * @param content
	 * @return 解码后的字符串
	 */
	public static String unescape(String content) {
		if(StrUtil.isBlank(content)) {
			return content;
		}
		
		StringBuilder tmp = new StringBuilder(content.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < content.length()) {
			pos = content.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (content.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(content.substring(lastPos));
					lastPos = content.length();
				} else {
					tmp.append(content.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	
	/**
	 * 安全的unescape文本，当文本不是被escape的时候，返回原文。
	 * @param content 内容
	 * @return 解码后的字符串，如果解码失败返回原字符串
	 */
	public static String safeUnescape(String content) {
		try {
			return unescape(content);
		} catch (Exception e) {
		}
		return content;
	}
}
