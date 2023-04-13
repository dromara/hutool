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
import org.dromara.hutool.extra.expression.ExpressionEngine;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Spring-Expression引擎封装<br>
 * 见：https://github.com/spring-projects/spring-framework/tree/master/spring-expression
 *
 * @since 5.5.0
 * @author looly
 */
public class SpELEngine extends SimpleWrapper<ExpressionParser> implements ExpressionEngine {

	/**
	 * 构造
	 */
	public SpELEngine(){
		super(new SpelExpressionParser());
	}

	@Override
	public Expression compile(final String expression) {
		return new SpELExpression(this.raw.parseExpression(expression));
	}
}
