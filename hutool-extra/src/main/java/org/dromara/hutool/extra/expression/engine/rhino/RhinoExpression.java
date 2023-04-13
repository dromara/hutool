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

package org.dromara.hutool.extra.expression.engine.rhino;

import org.dromara.hutool.core.func.SimpleWrapper;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.extra.expression.Expression;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Map;

/**
 * rhino引擎表达式包装
 *
 * @author looly
 */
public class RhinoExpression extends SimpleWrapper<String>
	implements Expression {

	private static final String SOURCE_RHINO_JS = "rhino.js";

	/**
	 * 构造
	 *
	 * @param expression 表达式
	 */
	public RhinoExpression(final String expression) {
		super(expression);
	}

	@Override
	public Object eval(final Map<String, Object> context) {
		try (final Context ctx = Context.enter()) {
			final Scriptable scope = ctx.initStandardObjects();
			if (MapUtil.isNotEmpty(context)) {
				context.forEach((key, value) -> {
					// 将java对象转为js对象后放置于JS的作用域中
					ScriptableObject.putProperty(scope, key, Context.javaToJS(value, scope));
				});
			}
			return ctx.evaluateString(scope, this.raw,
				SOURCE_RHINO_JS, 1, null);
		} // auto close
	}
}
