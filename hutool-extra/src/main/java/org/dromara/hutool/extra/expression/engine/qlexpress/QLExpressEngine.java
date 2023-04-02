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

import org.dromara.hutool.extra.expression.ExpressionEngine;
import org.dromara.hutool.extra.expression.ExpressionException;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

import java.util.Map;

/**
 * QLExpress引擎封装<br>
 * 见：https://github.com/alibaba/QLExpress
 *
 * @author looly
 * @since 5.8.9
 */
public class QLExpressEngine implements ExpressionEngine {

	private final ExpressRunner engine;

	/**
	 * 构造
	 */
	public QLExpressEngine() {
		engine = new ExpressRunner();
	}

	@Override
	public Object eval(final String expression, final Map<String, Object> context) {
		final DefaultContext<String, Object> defaultContext = new DefaultContext<>();
		defaultContext.putAll(context);
		try {
			return engine.execute(expression, defaultContext, null, true, false);
		} catch (final Exception e) {
			throw new ExpressionException(e);
		}
	}
}
