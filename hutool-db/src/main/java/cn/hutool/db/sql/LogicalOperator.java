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

package cn.hutool.db.sql;

import cn.hutool.core.text.StrUtil;

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
	public boolean isSame(final String logicalOperatorStr) {
		if(StrUtil.isBlank(logicalOperatorStr)) {
			return false;
		}
		return this.name().equalsIgnoreCase(logicalOperatorStr.trim());
	}
}
