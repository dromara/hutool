package cn.hutool.extra.expression.engine.jfireel;

import cn.hutool.extra.expression.ExpressionEngine;
import com.jfirer.jfireel.expression.Expression;

import java.util.Map;

/**
 * JfireEL引擎封装<br>
 * 见：https://gitee.com/eric_ds/jfireEL
 *
 * @since 5.5.0
 * @author looly
 */
public class JfireELEngine implements ExpressionEngine {

	/**
	 * 构造
	 */
	public JfireELEngine(){
	}

	@Override
	public Object eval(String expression, Map<String, Object> context) {
		return Expression.parse(expression).calculate(context);
	}
}
