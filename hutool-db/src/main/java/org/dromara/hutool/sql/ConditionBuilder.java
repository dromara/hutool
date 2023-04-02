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

package org.dromara.hutool.sql;

import org.dromara.hutool.lang.builder.Builder;
import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.util.CharUtil;
import org.dromara.hutool.text.StrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 多条件构建封装<br>
 * 可以将多个条件构建为SQL语句的一部分，并将参数值转换为占位符，并提取对应位置的参数值。<br>
 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
 *
 * @author looly
 * @since 5.4.3
 */
public class ConditionBuilder implements Builder<String> {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建构建器
	 *
	 * @param conditions 条件列表
	 * @return ConditionBuilder
	 */
	public static ConditionBuilder of(final Condition... conditions) {
		return new ConditionBuilder(conditions);
	}

	/**
	 * 条件数组
	 */
	private final Condition[] conditions;
	/**
	 * 占位符对应的值列表
	 */
	private List<Object> paramValues;

	/**
	 * 构造
	 *
	 * @param conditions 条件列表
	 */
	public ConditionBuilder(final Condition... conditions) {
		this.conditions = conditions;
	}

	/**
	 * 返回构建后的参数列表<br>
	 * 此方法调用前必须调用{@link #build()}
	 *
	 * @return 参数列表
	 */
	public List<Object> getParamValues() {
		return ListUtil.view(this.paramValues);
	}

	/**
	 * 构建组合条件<br>
	 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
	 *
	 * @return 构建后的SQL语句条件部分
	 */
	@Override
	public String build() {
		if(null == this.paramValues){
			this.paramValues = new ArrayList<>();
		} else {
			this.paramValues.clear();
		}
		return build(this.paramValues);
	}

	/**
	 * 构建组合条件<br>
	 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
	 *
	 * @param paramValues 用于写出参数的List,构建时会将参数写入此List
	 * @return 构建后的SQL语句条件部分
	 */
	public String build(final List<Object> paramValues) {
		if (ArrayUtil.isEmpty(conditions)) {
			return StrUtil.EMPTY;
		}

		final StringBuilder conditionStrBuilder = new StringBuilder();
		boolean isFirst = true;
		for (final Condition condition : conditions) {
			// 添加逻辑运算符
			if (isFirst) {
				isFirst = false;
			} else {
				// " AND " 或者 " OR "
				conditionStrBuilder.append(CharUtil.SPACE).append(condition.getLinkOperator()).append(CharUtil.SPACE);
			}

			// 构建条件部分："name = ?"、"name IN (?,?,?)"、"name BETWEEN ？AND ？"、"name LIKE ?"
			conditionStrBuilder.append(condition.toString(paramValues));
		}
		return conditionStrBuilder.toString();
	}

	/**
	 * 构建组合条件<br>
	 * 例如：name = ? AND type IN (?, ?) AND other LIKE ?
	 *
	 * @return 构建后的SQL语句条件部分
	 */
	@Override
	public String toString() {
		return build();
	}
}
