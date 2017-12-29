package com.xiaoleilu.hutool.text.csv;

public class CsvParser {
	
	/** 上一行的缓存，在多行模式下缓存上一行 */
	private String pending;
	/** 是否在字段内 */
	private boolean inField = false;

	public static class ParserConfig{
		/** 分隔符 */
		char separator;
		/** 字符串包装符 */
		char quotechar;
		/** 转义符 */
		char escape;
		/** 每个字段是否去除首尾空白符 */
		boolean isTrim;
	}
}
