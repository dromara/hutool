package com.xiaoleilu.hutool.cron.parser;

/**
 * 值处理接口
 * @author Looly
 *
 */
public interface ValueParser {
	
	/**
	 * 处理String值并转为int
	 * @param value String值
	 * @return int
	 */
	public int parse(String value);
	
	/**
	 * 返回最小值
	 * @return 最小值
	 */
	public int getMin();
	
	/**
	 * 返回最大值
	 * @return 最大值
	 */
	public int getMax();
}
