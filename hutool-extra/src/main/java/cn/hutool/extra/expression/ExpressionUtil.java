package cn.hutool.extra.expression;

import cn.hutool.extra.expression.engine.ExpressionFactory;

import java.util.Map;

/**
 * 表达式引擎工具类
 *
 * @author looly
 * @since 5.5.0
 */
public class ExpressionUtil {

	/**
	 * 获得全局单例的表达式引擎
	 *
	 * @return 全局单例的表达式引擎
	 */
	public static ExpressionEngine getEngine() {
		return ExpressionFactory.get();
	}

	/**
	 * 执行表达式
	 *
	 * @param expression 表达式
	 * @param context    表达式上下文，用于存储表达式中所需的变量值等
	 * @return 执行结果
	 */
	public static Object eval(String expression, Map<String, Object> context) {
		return getEngine().eval(expression, context);
	}
}
