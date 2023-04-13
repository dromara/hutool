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

package org.dromara.hutool.extra.expression.engine.mvel;

import org.dromara.hutool.core.func.SimpleWrapper;
import org.dromara.hutool.extra.expression.Expression;
import org.mvel2.MVEL;
import org.mvel2.templates.TemplateCompiler;

import java.util.Map;

/**
 * MVEL2的{@link TemplateCompiler}包装
 *
 * @author looly
 */
public class MvelExpression extends SimpleWrapper<String>
	implements Expression {

	/**
	 * 构造
	 *
	 * @param expression 表达式字符串
	 */
	public MvelExpression(final String expression) {
		super(expression);
	}

	@Override
	public Object eval(final Map<String, Object> context) {
		return MVEL.eval(this.raw, context);
	}
}
