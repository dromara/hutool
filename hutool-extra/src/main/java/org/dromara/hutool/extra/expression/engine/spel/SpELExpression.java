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

package org.dromara.hutool.extra.expression.engine.spel;

import org.dromara.hutool.core.func.SimpleWrapper;
import org.dromara.hutool.extra.expression.Expression;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * Spring-Expression引擎表达式{@link org.springframework.expression.Expression} 包装
 *
 * @author looly
 */
public class SpELExpression extends SimpleWrapper<org.springframework.expression.Expression>
	implements Expression {

	/**
	 * 构造
	 *
	 * @param expression {@link org.springframework.expression.Expression}
	 */
	public SpELExpression(final org.springframework.expression.Expression expression) {
		super(expression);
	}

	@Override
	public Object eval(final Map<String, Object> context) {
		final EvaluationContext evaluationContext = new StandardEvaluationContext();
		context.forEach(evaluationContext::setVariable);

		return this.raw.getValue(evaluationContext);
	}
}
