package cn.hutool.extra.pinyin;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * 拼音工具类，封装了TinyPinyin
 *
 * <p>
 * TinyPinyin(https://github.com/promeG/TinyPinyin)提供者未提交Maven中央库，<br>
 * 因此使用
 * https://github.com/biezhi/TinyPinyin打包的版本
 * </p>
 *
 * <p>
 * 引入：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;io.github.biezhi&lt;/groupId&gt;
 *     &lt;artifactId&gt;TinyPinyin&lt;/artifactId&gt;
 *     &lt;version&gt;2.0.3.RELEASE&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * @author looly
 */
public class PinyinUtil {

	/**
	 * 自定义拼音全局配置，例如加入自定义字典等
	 *
	 * @param config 配置，通过Pinyin.newConfig().with(dict)添加字典
	 */
	public static void init(Pinyin.Config config) {
		Pinyin.init(config);
	}

	/**
	 * 如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
	 *
	 * @param c             任意字符，汉字返回拼音，非汉字原样返回
	 * @param isToUpperCase 是否转换为大写
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(char c, boolean isToUpperCase) {
		final String pinyin = Pinyin.toPinyin(c);
		return isToUpperCase ? pinyin : pinyin.toLowerCase();
	}

	/**
	 * 将输入字符串转为拼音，每个字之间的拼音使用空格分隔
	 *
	 * @param str           任意字符，汉字返回拼音，非汉字原样返回
	 * @param isToUpperCase 是否转换为大写
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(String str, boolean isToUpperCase) {
		return getPinyin(str, " ", isToUpperCase);
	}

	/**
	 * 将输入字符串转为拼音，以字符为单位插入分隔符
	 *
	 * @param str           任意字符，汉字返回拼音，非汉字原样返回
	 * @param separator     每个字拼音之间的分隔符
	 * @param isToUpperCase 是否转换为大写
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static String getPinyin(String str, String separator, boolean isToUpperCase) {
		final String pinyin = Pinyin.toPinyin(str, separator);
		return isToUpperCase ? pinyin : pinyin.toLowerCase();
	}

	/**
	 * 将输入字符串转为拼音首字母，其它字符原样返回
	 *
	 * @param str           任意字符，汉字返回拼音，非汉字原样返回
	 * @param isToUpperCase 是否转换为大写
	 * @return 汉字返回拼音，非汉字原样返回
	 */
	public static char[] getFirstLetter(String str, boolean isToUpperCase) {
		final char[] result = new char[str.length()];
		for(int i=0; i < result.length; i++){
			result[i] = getPinyin(str.charAt(i), isToUpperCase).charAt(0);
		}
		return result;
	}
}
