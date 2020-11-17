package cn.hutool.extra.expression.engine.rhino;

import cn.hutool.extra.expression.ExpressionEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Map;

/**
 * rhino引擎封装<br>
 * 见：https://github.com/mozilla/rhino
 *
 * @since 5.5.2
 * @author lzpeng
 */
public class RhinoEngine implements ExpressionEngine {

	/**
	 * 构造
	 */
	public RhinoEngine(){
	}

	@Override
	public Object eval(String expression, Map<String, Object> context) {
		Context ctx = Context.enter();
		Scriptable scope = ctx.initStandardObjects();
		if (context != null && !context.isEmpty()) {
			for (Map.Entry<String, Object> entry : context.entrySet()) {
				// 将java对象转为js对象
				Object jsObj = Context.javaToJS(entry.getValue(), scope);
				// 将java对象放置JS的作用域中
				ScriptableObject.putProperty(scope, entry.getKey(), jsObj);
			}
		}
		Object result = ctx.evaluateString(scope, expression, "rhino.js", 1, null);
		Context.exit();
		return result;
	}
}
