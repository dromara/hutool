package cn.hutool.extra.expression.engine.jexl;

import cn.hutool.extra.expression.ExpressionEngine;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.MapContext;

import java.util.Map;

/**
 * Jexl3引擎封装<br>
 * 见：https://github.com/apache/commons-jexl
 *
 * @since 5.5.0
 * @author looly
 */
public class JexlEngine implements ExpressionEngine {

	private final org.apache.commons.jexl3.JexlEngine engine;

	public JexlEngine(){
		engine = (new JexlBuilder()).cache(512).strict(true).silent(false).create();
	}

	@Override
	public Object eval(String expression, Map<String, Object> context) {
		final MapContext mapContext = new MapContext(context);

		try{
			return engine.createExpression(expression).evaluate(mapContext);
		} catch (Exception ignore){
			// https://gitee.com/dromara/hutool/issues/I4B70D
			// 支持脚本
			return engine.createScript(expression).execute(mapContext);
		}
	}

	/**
	 * 获取{@link org.apache.commons.jexl3.JexlEngine}
	 *
	 * @return {@link org.apache.commons.jexl3.JexlEngine}
	 */
	public org.apache.commons.jexl3.JexlEngine getEngine() {
		return this.engine;
	}
}
