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

package org.dromara.hutool.extra.expression.engine.qlexpress;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.dromara.hutool.core.func.SimpleWrapper;
import org.dromara.hutool.extra.expression.Expression;
import org.dromara.hutool.extra.expression.ExpressionException;

import java.util.Map;

/**
 * QLExpress引擎表达式封装<br>
 * 由于QLExpress引擎使用字符串缓存方式存储表达式树，故此处无需存储表达式对象
 *
 * @author looly
 */
public class QLExpressExpression extends SimpleWrapper<String>
	implements Expression {

	private final ExpressRunner engine;

	/**
	 * 构造
	 *
	 * @param engine     {@link ExpressRunner}
	 * @param expression 表达式字符串
	 */
	public QLExpressExpression(final ExpressRunner engine, final String expression) {
		super(expression);
		this.engine = engine;
	}

	@Override
	public Object eval(final Map<String, Object> context) {
		final DefaultContext<String, Object> defaultContext = new DefaultContext<>();
		defaultContext.putAll(context);

		try {
			return engine.execute(this.raw, defaultContext, null, true, false);
		} catch (final Exception e) {
			throw new ExpressionException(e);
		}
	}
}
