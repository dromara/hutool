/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.List;

/**
 * 条件组<br>
 * 用于构建复杂where条件
 *
 * @author tjh
 * @since 5.7.21
 */
public class ConditionGroup extends Condition {
	private static final long serialVersionUID = 1L;
	/**
	 * 条件列表
	 */
	private Condition[] conditions;

	/**
	 * 追加条件
	 *
	 * @param conditions 条件列表
	 */
	public void addConditions(final Condition... conditions) {
		if (null == this.conditions) {
			this.conditions = conditions;
		} else {
			this.conditions = ArrayUtil.addAll(this.conditions, conditions);
		}
	}

	/**
	 * 将条件组转换为条件字符串，使用括号包裹，并回填占位符对应的参数值
	 *
	 * @param paramValues 参数列表，用于回填占位符对应参数值
	 * @return 条件字符串
	 */
	@Override
	public String toString(final List<Object> paramValues) {
		if (ArrayUtil.isEmpty(conditions)) {
			return StrUtil.EMPTY;
		}

		final StringBuilder conditionStrBuilder = StrUtil.builder();
		conditionStrBuilder.append("(");
		// 将组内的条件构造为SQL，因为toString，会进行递归，处理所有的条件组
		conditionStrBuilder.append(ConditionBuilder.of(this.conditions).build(paramValues));
		conditionStrBuilder.append(")");

		return conditionStrBuilder.toString();
	}
}
