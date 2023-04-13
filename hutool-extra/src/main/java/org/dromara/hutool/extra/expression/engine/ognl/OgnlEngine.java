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
import ognl.OgnlException;
import org.dromara.hutool.extra.expression.Expression;
import org.dromara.hutool.extra.expression.ExpressionEngine;
import org.dromara.hutool.extra.expression.ExpressionException;

/**
 * OGNL(Object-Graph Navigation Language)表达式引擎封装<br>
 * 见：https://github.com/orphan-oss/ognl
 *
 * @author looly
 */
public class OgnlEngine implements ExpressionEngine {
	@Override
	public Expression compile(final String expression) {
		try {
			return new OgnlExpression(Ognl.parseExpression(expression));
		} catch (final OgnlException e) {
			throw new ExpressionException(e);
		}
	}
}
