package cn.hutool.extra.expression.engine.mvel;

import cn.hutool.extra.expression.ExpressionEngine;
import org.mvel2.MVEL;

import java.util.Collection;
import java.util.Map;

/**
 * MVEL (MVFLEX Expression Language)引擎封装<br>
 * 见：https://github.com/mvel/mvel
 *
 * @since 5.5.0
 * @author looly
 */
public class MvelEngine implements ExpressionEngine {

	static {
		checkEngineExist(MVEL.class);
	}

	/**
	 * 构造
	 */
	public MvelEngine(){
	}

	@Override
	public Object eval(String expression, Map<String, Object> context, Collection<Class<?>> allowClassSet) {
		return MVEL.eval(expression, context);
	}

	private static void checkEngineExist(Class<?> clazz){
		// do nothing
	}
}
