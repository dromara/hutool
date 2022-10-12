package cn.hutool.extra.expression.engine.qlexpress;

import cn.hutool.extra.expression.ExpressionEngine;
import cn.hutool.extra.expression.ExpressionException;
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
