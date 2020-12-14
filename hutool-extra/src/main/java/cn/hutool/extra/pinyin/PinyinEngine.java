package cn.hutool.extra.pinyin;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 拼音引擎接口，具体的拼音实现通过实现此接口，完成具体实现功能
 *
 * @author looly
 * @since 5.3.3
 */
public interface PinyinEngine {

	/**
	 * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
	 *
	 * @param c 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	String getPinyin(char c);

	/**
	 * 获取字符串对应的完整拼音，非中文返回原字符
	 *
	 * @param str 字符串
	 * @param separator 拼音之间的分隔符
	 * @return 拼音
	 */
	String getPinyin(String str, String separator);

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param c 任意字符，汉字返回拼音，非汉字原样返回
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	default char getFirstLetter(char c) {
		return getPinyin(c).charAt(0);
	}

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param str 任意字符，汉字返回拼音，非汉字原样返回
	 * @param separator 分隔符
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	default String getFirstLetter(String str, String separator) {
		final String splitSeparator = StrUtil.isEmpty(separator) ? "#" : separator;
		final String[] split = StrUtil.split(getPinyin(str, splitSeparator), splitSeparator);
		return ArrayUtil.join(split, separator, (s)->String.valueOf(s.length() > 0 ? s.charAt(0) : ""));
	}
}
