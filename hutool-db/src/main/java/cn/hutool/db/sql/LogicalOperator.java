package cn.hutool.db.sql;

import cn.hutool.core.util.StrUtil;

/**
 * 逻辑运算符
 * @author Looly
 *
 */
public enum LogicalOperator{
	/** 且，两个条件都满足 */
	AND,
	/** 或，满足多个条件的一个即可 */
	OR;
	
	/**
	 * 给定字符串逻辑运算符是否与当前逻辑运算符一致，不区分大小写，自动去除两边空白符
	 * 
	 * @param logicalOperatorStr 逻辑运算符字符串
	 * @return 是否与当前逻辑运算符一致
	 * @since 3.2.1
	 */
	public boolean isSame(String logicalOperatorStr) {
		if(StrUtil.isBlank(logicalOperatorStr)) {
			return false;
		}
		return this.name().equalsIgnoreCase(logicalOperatorStr.trim());
	}
}