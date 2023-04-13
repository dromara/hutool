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

package org.dromara.hutool.extra.expression.engine.ognl;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.dromara.hutool.core.func.SimpleWrapper;
import org.dromara.hutool.extra.expression.Expression;
import org.dromara.hutool.extra.expression.ExpressionException;

import java.util.Map;

/**
 * OGNL表达式包装
 *
 * @author looly
 */
public class OgnlExpression extends SimpleWrapper<Object> implements Expression {

	/**
	 * 构造
	 *
	 * @param expression 表达式对象
	 */
	public OgnlExpression(final Object expression) {
		super(expression);
	}

	@Override
	public Object eval(final Map<String, Object> context) {
		final OgnlContext ognlContext = new OgnlContext(context);
		try {
			return Ognl.getValue(this.raw, ognlContext, ognlContext.getRoot());
		} catch (final OgnlException e) {
			throw new ExpressionException(e);
		}
	}
}
