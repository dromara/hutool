package cn.hutool.extra.expression.engine.aviator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.expression.ExpressionEngine;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Aviator引擎封装<br>
 * 见：https://github.com/killme2008/aviatorscript
 *
 * @author looly
 * @since 5.5.0
 */
public class AviatorEngine implements ExpressionEngine {

	private final AviatorEvaluatorInstance engine;

	/**
	 * 构造
	 */
	public AviatorEngine() {
		engine = AviatorEvaluator.getInstance();
	}

	@Override
	public Object eval(String expression, Map<String, Object> context, Collection<Class<?>> allowClassSet) {
		// issue#I6AJWJ
		engine.setOption(Options.ALLOWED_CLASS_SET,
			CollUtil.isEmpty(allowClassSet) ? Collections.emptySet() : CollUtil.newHashSet(allowClassSet));
		return engine.execute(expression, context);
	}

	/**
	 * 获取{@link AviatorEvaluatorInstance}
	 *
	 * @return {@link AviatorEvaluatorInstance}
	 */
	public AviatorEvaluatorInstance getEngine() {
		return this.engine;
	}
}
