package cn.hutool.cron.pattern.parser;

/**
 * 值处理接口<br>
 * 值处理用于限定表达式中相应位置的值范围，并转换表达式值为int值
 *
 * @author Looly
 */
public interface ValueParser {

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
