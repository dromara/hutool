package cn.hutool.extra.pinyin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.engine.PinyinFactory;

/**
 * 拼音工具类，封装了TinyPinyin、JPinyin、Pinyin4j，通过SPI自动识别。
 *
 * @author looly
 */
public class PinyinUtil {

	private static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]";

	/**
	 * 获得全局单例的拼音引擎
	 *
	 * @return 全局单例的拼音引擎
	 */
	public static PinyinEngine getEngine(){
		return PinyinFactory.get();
	}

	/**
	 * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
	 *
	 * @param c 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(char c) {
		return getEngine().getPinyin(c);
	}

	/**
	 * 将输入字符串转为拼音，每个字之间的拼音使用空格分隔
	 *
	 * @param str 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(String str) {
		return getPinyin(str, StrUtil.SPACE);
	}

	/**
	 * 将输入字符串转为拼音，以字符为单位插入分隔符
	 *
	 * @param str       任意字符，汉字返回拼音，非汉字原样返回
	 * @param separator 每个字拼音之间的分隔符
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(String str, String separator) {
		return getEngine().getPinyin(str, separator);
	}

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param c 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static char getFirstLetter(char c) {
		return getEngine().getFirstLetter(c);
	}

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param str       任意字符，汉字返回拼音，非汉字原样返回
	 * @param separator 分隔符
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getFirstLetter(String str, String separator) {
		return getEngine().getFirstLetter(str, separator);
	}

	/**
	 * 是否为中文字符
	 *
	 * @param c 字符
	 * @return 是否为中文字符
	 */
	public static boolean isChinese(char c) {
		return '〇' == c || String.valueOf(c).matches(CHINESE_REGEX);
	}
}
