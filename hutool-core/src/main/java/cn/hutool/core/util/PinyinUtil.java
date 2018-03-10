package cn.hutool.core.util;

import cn.hutool.core.text.StrBuilder;

/**
 * 拼音工具类
 * 
 * @author looly
 * @since 4.0.7
 */
public class PinyinUtil {

	/**
	 * 获取所给中文的每个汉字首字母组成首字母字符串
	 * @param chinese 汉字字符串
	 * @return 首字母字符串
	 */
	public static String getAllFirstLetter(String chinese) {
		if (StrUtil.isBlank(chinese)) {
			return StrUtil.EMPTY;
		}
		
		int len = chinese.length();
		final StrBuilder strBuilder = new StrBuilder(len);
		for (int i = 0; i < len; i++) {
			strBuilder.append(getFirstLetter(chinese.charAt(i)));
		}

		return strBuilder.toString();
	}

	/**
	 * 获取拼音首字母<br>
	 * 传入汉字，返回拼音首字母<br>
	 * 如果传入为字母，返回其小写形式<br>
	 * 感谢【帝都】宁静 提供方法
	 * 
	 * @param ch 汉字
	 * @return 首字母，小写
	 */
	public static char getFirstLetter(char ch) {
		if (ch >= 'a' && ch <= 'z') {
			return ch;
		}
		if (ch >= 'A' && ch <= 'Z') {
			return Character.toLowerCase(ch);
		}
		final byte[] bys = String.valueOf(ch).getBytes(CharsetUtil.CHARSET_GBK);
		if (bys.length == 1) {
			return '*';
		}
		int count = (bys[0] + 256) * 256 + bys[1] + 256;
		if (count < 45217) {
			return '*';
		} else if (count < 45253) {
			return 'a';
		} else if (count < 45761) {
			return 'b';
		} else if (count < 46318) {
			return 'c';
		} else if (count < 46826) {
			return 'd';
		} else if (count < 47010) {
			return 'e';
		} else if (count < 47297) {
			return 'f';
		} else if (count < 47614) {
			return 'g';
		} else if (count < 48119) {
			return 'h';
		} else if (count < 49062) {
			return 'j';
		} else if (count < 49324) {
			return 'k';
		} else if (count < 49896) {
			return 'l';
		} else if (count < 50371) {
			return 'm';
		} else if (count < 50614) {
			return 'n';
		} else if (count < 50622) {
			return 'o';
		} else if (count < 50906) {
			return 'p';
		} else if (count < 51387) {
			return 'q';
		} else if (count < 51446) {
			return 'r';
		} else if (count < 52218) {
			return 's';
		} else if (count < 52698) {
			return 't';
		} else if (count < 52980) {
			return 'w';
		} else if (count < 53689) {
			return 'x';
		} else if (count < 54481) {
			return 'y';
		} else if (count < 55290) {
			return 'z';
		}
		return ' ';
	}
}
