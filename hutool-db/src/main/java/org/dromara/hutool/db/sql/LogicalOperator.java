/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.text.StrUtil;

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
