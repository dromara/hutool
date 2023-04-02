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

package org.dromara.hutool.extra.expression.engine.jfireel;

import org.dromara.hutool.extra.expression.ExpressionEngine;
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
	public Object eval(final String expression, final Map<String, Object> context) {
		return Expression.parse(expression).calculate(context);
	}
}
