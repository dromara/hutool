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

package cn.hutool.extra.expression.engine.rhino;

import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.expression.ExpressionEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Map;

/**
 * rhino引擎封装<br>
 * 见：https://github.com/mozilla/rhino
 *
 * @author lzpeng
 * @since 5.5.2
 */
public class RhinoEngine implements ExpressionEngine {

	@Override
	public Object eval(final String expression, final Map<String, Object> context) {
		try(final Context ctx = Context.enter()){
			final Scriptable scope = ctx.initStandardObjects();
			if (MapUtil.isNotEmpty(context)) {
				context.forEach((key, value)->{
					// 将java对象转为js对象后放置于JS的作用域中
					ScriptableObject.putProperty(scope, key, Context.javaToJS(value, scope));
				});
			}
			return ctx.evaluateString(scope, expression, "rhino.js", 1, null);
		} // auto close
	}
}
