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

package org.dromara.hutool.extra.expression.engine.aviator;

import org.dromara.hutool.extra.expression.ExpressionEngine;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;

import java.util.Map;

/**
 * Aviator引擎封装<br>
 * 见：<a href="https://github.com/killme2008/aviatorscript">https://github.com/killme2008/aviatorscript</a>
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
	public Object eval(final String expression, final Map<String, Object> context) {
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
