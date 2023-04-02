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

package org.dromara.hutool.text;

/**
 * 检测密码强度<br>
 * 来自：<a href="https://github.com/venshine/CheckPasswordStrength">https://github.com/venshine/CheckPasswordStrength</a>
 *
 * @author venshine
 * @since 5.7.3
 */
public class PasswdStrength {

	/**
	 * 密码强度等级枚举
	 */
	public enum PASSWD_LEVEL {
		/**
		 * 简单
		 */
		EASY,
		/**
		 * 中
		 */
		MEDIUM,
		/**
		 * 强
		 */
		STRONG,
		/**
		 * 很强
		 */
		VERY_STRONG,
		/**
		 * 非常强
		 */
		EXTREMELY_STRONG
	}

	/**
	 * 字符类型枚举
	 */
	public enum CHAR_TYPE {
		/**
		 * 数字
		 */
		NUM,
		/**
		 * 小写字母
		 */
		SMALL_LETTER,
		/**
		 * 大写字母
		 */
		CAPITAL_LETTER,
		/**
		 * 特殊字符
		 */
		OTHER_CHAR
	}

	/**
	 * 简单密码字典
	 */
	private final static String[] DICTIONARY = {"password", "abc123", "iloveyou", "adobe123", "123123", "sunshine",
			"1314520", "a1b2c3", "123qwe", "aaa111", "qweasd", "admin", "passwd"};

	/**
	 * 数字长度
	 */
	private final static int[] SIZE_TABLE = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999,
			Integer.MAX_VALUE};

	/**
	 * 检查密码的健壮性
	 *
	 * @param passwd 密码
	 * @return strength level
	 */
	public static int check(final String passwd) {
		if (null == passwd) {
			throw new IllegalArgumentException("password is empty");
		}
		final int len = passwd.length();
		int level = 0;

		// increase points
		if (countLetter(passwd, CHAR_TYPE.NUM) > 0) {
			level++;
		}
		if (countLetter(passwd, CHAR_TYPE.SMALL_LETTER) > 0) {
			level++;
		}
		if (len > 4 && countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) > 0) {
			level++;
		}
		if (len > 6 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) > 0) {
			level++;
		}

		if (len > 4 && countLetter(passwd, CHAR_TYPE.NUM) > 0 && countLetter(passwd, CHAR_TYPE.SMALL_LETTER) > 0
				|| countLetter(passwd, CHAR_TYPE.NUM) > 0 && countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) > 0
				|| countLetter(passwd, CHAR_TYPE.NUM) > 0 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) > 0
				|| countLetter(passwd, CHAR_TYPE.SMALL_LETTER) > 0 && countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) > 0
				|| countLetter(passwd, CHAR_TYPE.SMALL_LETTER) > 0 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) > 0
				|| countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) > 0 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) > 0) {
			level++;
		}

		if (len > 6 && countLetter(passwd, CHAR_TYPE.NUM) > 0 && countLetter(passwd, CHAR_TYPE.SMALL_LETTER) > 0
				&& countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) > 0 || countLetter(passwd, CHAR_TYPE.NUM) > 0
				&& countLetter(passwd, CHAR_TYPE.SMALL_LETTER) > 0 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) > 0
				|| countLetter(passwd, CHAR_TYPE.NUM) > 0 && countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) > 0
				&& countLetter(passwd, CHAR_TYPE.OTHER_CHAR) > 0 || countLetter(passwd, CHAR_TYPE.SMALL_LETTER) > 0
				&& countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) > 0 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) > 0) {
			level++;
		}

		if (len > 8 && countLetter(passwd, CHAR_TYPE.NUM) > 0 && countLetter(passwd, CHAR_TYPE.SMALL_LETTER) > 0
				&& countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) > 0 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) > 0) {
			level++;
		}

		if (len > 6 && countLetter(passwd, CHAR_TYPE.NUM) >= 3 && countLetter(passwd, CHAR_TYPE.SMALL_LETTER) >= 3
				|| countLetter(passwd, CHAR_TYPE.NUM) >= 3 && countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) >= 3
				|| countLetter(passwd, CHAR_TYPE.NUM) >= 3 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 2
				|| countLetter(passwd, CHAR_TYPE.SMALL_LETTER) >= 3 && countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) >= 3
				|| countLetter(passwd, CHAR_TYPE.SMALL_LETTER) >= 3 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 2
				|| countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) >= 3 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 2) {
			level++;
		}

		if (len > 8 && countLetter(passwd, CHAR_TYPE.NUM) >= 2 && countLetter(passwd, CHAR_TYPE.SMALL_LETTER) >= 2
				&& countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) >= 2 || countLetter(passwd, CHAR_TYPE.NUM) >= 2
				&& countLetter(passwd, CHAR_TYPE.SMALL_LETTER) >= 2 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 2
				|| countLetter(passwd, CHAR_TYPE.NUM) >= 2 && countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) >= 2
				&& countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 2 || countLetter(passwd, CHAR_TYPE.SMALL_LETTER) >= 2
				&& countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) >= 2 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 2) {
			level++;
		}

		if (len > 10 && countLetter(passwd, CHAR_TYPE.NUM) >= 2 && countLetter(passwd, CHAR_TYPE.SMALL_LETTER) >= 2
				&& countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) >= 2 && countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 2) {
			level++;
		}

		if (countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 3) {
			level++;
		}
		if (countLetter(passwd, CHAR_TYPE.OTHER_CHAR) >= 6) {
			level++;
		}

		if (len > 12) {
			level++;
			if (len >= 16) {
				level++;
			}
		}

		// decrease points
		if ("abcdefghijklmnopqrstuvwxyz".indexOf(passwd) > 0 || "ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(passwd) > 0) {
			level--;
		}
		if ("qwertyuiop".indexOf(passwd) > 0 || "asdfghjkl".indexOf(passwd) > 0 || "zxcvbnm".indexOf(passwd) > 0) {
			level--;
		}
		if (StrUtil.isNumeric(passwd) && ("01234567890".indexOf(passwd) > 0 || "09876543210".indexOf(passwd) > 0)) {
			level--;
		}

		if (countLetter(passwd, CHAR_TYPE.NUM) == len || countLetter(passwd, CHAR_TYPE.SMALL_LETTER) == len
				|| countLetter(passwd, CHAR_TYPE.CAPITAL_LETTER) == len) {
			level--;
		}

		if (len % 2 == 0) { // aaabbb
			final String part1 = passwd.substring(0, len / 2);
			final String part2 = passwd.substring(len / 2);
			if (part1.equals(part2)) {
				level--;
			}
			if (StrUtil.isCharEquals(part1) && StrUtil.isCharEquals(part2)) {
				level--;
			}
		}
		if (len % 3 == 0) { // ababab
			final String part1 = passwd.substring(0, len / 3);
			final String part2 = passwd.substring(len / 3, len / 3 * 2);
			final String part3 = passwd.substring(len / 3 * 2);
			if (part1.equals(part2) && part2.equals(part3)) {
				level--;
			}
		}

		if (StrUtil.isNumeric(passwd) && len >= 6 && len <= 8) { // 19881010 or 881010
			int year = 0;
			if (len == 8 || len == 6) {
				year = Integer.parseInt(passwd.substring(0, len - 4));
			}
			final int size = sizeOfInt(year);
			final int month = Integer.parseInt(passwd.substring(size, size + 2));
			final int day = Integer.parseInt(passwd.substring(size + 2, len));
			if (year >= 1950 && year < 2050 && month >= 1 && month <= 12 && day >= 1 && day <= 31) {
				level--;
			}
		}

		for (final String s : DICTIONARY) {
			if (passwd.equals(s) || s.contains(passwd)) {
				level--;
				break;
			}
		}

		if (len <= 6) {
			level--;
			if (len <= 4) {
				level--;
				if (len <= 3) {
					level = 0;
				}
			}
		}

		if (StrUtil.isCharEquals(passwd)) {
			level = 0;
		}

		if (level < 0) {
			level = 0;
		}

		return level;
	}

	/**
	 * Get password strength level, includes easy, midium, strong, very strong, extremely strong
	 *
	 * @param passwd 密码
	 * @return 密码等级枚举
	 */
	public static PASSWD_LEVEL getLevel(final String passwd) {
		final int level = check(passwd);
		switch (level) {
			case 0:
			case 1:
			case 2:
			case 3:
				return PASSWD_LEVEL.EASY;
			case 4:
			case 5:
			case 6:
				return PASSWD_LEVEL.MEDIUM;
			case 7:
			case 8:
			case 9:
				return PASSWD_LEVEL.STRONG;
			case 10:
			case 11:
			case 12:
				return PASSWD_LEVEL.VERY_STRONG;
			default:
				return PASSWD_LEVEL.EXTREMELY_STRONG;
		}
	}

	/**
	 * Check character's type, includes num, capital letter, small letter and other character.
	 * 检查字符类型
	 *
	 * @param c 字符
	 * @return 类型
	 */
	private static CHAR_TYPE checkCharacterType(final char c) {
		if (c >= 48 && c <= 57) {
			return CHAR_TYPE.NUM;
		}
		if (c >= 65 && c <= 90) {
			return CHAR_TYPE.CAPITAL_LETTER;
		}
		if (c >= 97 && c <= 122) {
			return CHAR_TYPE.SMALL_LETTER;
		}
		return CHAR_TYPE.OTHER_CHAR;
	}

	/**
	 * 计算密码中指定字符类型的数量
	 *
	 * @param passwd 密码
	 * @param type   类型
	 * @return 数量
	 */
	private static int countLetter(final String passwd, final CHAR_TYPE type) {
		int count = 0;
		if (null != passwd) {
			final int length = passwd.length();
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					if (checkCharacterType(passwd.charAt(i)) == type) {
						count++;
					}
				}
			}
		}
		return count;
	}

	/**
	 * calculate the size of an integer number
	 *
	 * @param x 值
	 * @return 数字长度
	 */
	private static int sizeOfInt(final int x) {
		for (int i = 0; ; i++)
			if (x <= SIZE_TABLE[i]) {
				return i + 1;
			}
	}
}
