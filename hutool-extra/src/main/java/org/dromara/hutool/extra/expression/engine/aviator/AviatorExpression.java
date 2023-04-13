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

package org.dromara.hutool.extra.expression.engine.aviator;

import org.dromara.hutool.core.func.SimpleWrapper;
import org.dromara.hutool.extra.expression.Expression;

import java.util.Map;

/**
 * {@link com.googlecode.aviator.Expression} 包装
 *
 * @author looly
 * @since 6.0.0
 */
public class AviatorExpression extends SimpleWrapper<com.googlecode.aviator.Expression> implements Expression {

	/**
	 * 构造
	 *
	 * @param expression {@link com.googlecode.aviator.Expression}
	 */
	public AviatorExpression(final com.googlecode.aviator.Expression expression) {
		super(expression);
	}

	@Override
	public Object eval(final Map<String, Object> context) {
		return this.raw.execute(context);
	}
}
