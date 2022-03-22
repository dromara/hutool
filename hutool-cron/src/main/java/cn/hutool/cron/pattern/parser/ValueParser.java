package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.pattern.matcher.ValueMatcher;

/**
 * 值处理接口<br>
 * 值处理用于限定表达式中相应位置的值范围，并转换表达式值为int值
 *
 * @author Looly
 */
public interface ValueParser {

	/**
	 * 解析表达式对应部分为{@link ValueMatcher}，支持的表达式包括：
	 * <ul>
	 *     <li>单值或通配符形式，如 <strong>a</strong> 或 <strong>*</strong></li>
	 *     <li>数组形式，如 <strong>1,2,3</strong></li>
	 *     <li>间隔形式，如 <strong>a&#47;b</strong> 或 <strong>*&#47;b</strong></li>
	 *     <li>范围形式，如 <strong>3-8</strong></li>
	 * </ul>
	 *
	 * @param pattern 对应时间部分的表达式
	 * @return {@link ValueMatcher}
	 */
	ValueMatcher parseAsValueMatcher(String pattern);

	/**
	 * 处理String值并转为int<br>
	 * 转换包括：
	 * <ol>
	 * <li>数字字符串转为数字</li>
	 * <li>别名转为对应的数字（如月份和星期）</li>
	 * </ol>
	 *
	 * @param value String值
	 * @return int
	 */
	int parse(String value);

	/**
	 * 返回最小值
	 *
	 * @return 最小值
	 */
	int getMin();

	/**
	 * 返回最大值
	 *
	 * @return 最大值
	 */
	int getMax();
}
