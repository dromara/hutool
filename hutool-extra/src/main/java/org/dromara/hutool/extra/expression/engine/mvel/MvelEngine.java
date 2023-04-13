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

package org.dromara.hutool.extra.expression.engine.mvel;

import org.dromara.hutool.extra.expression.Expression;
import org.dromara.hutool.extra.expression.ExpressionEngine;

/**
 * MVEL (MVFLEX Expression Language)引擎封装<br>
 * 见：https://github.com/mvel/mvel
 *
 * @since 5.5.0
 * @author looly
 */
public class MvelEngine implements ExpressionEngine {

	/**
	 * 构造
	 */
	public MvelEngine(){
	}

	@Override
	public Expression compile(final String expression) {
		return new MvelExpression(expression);
	}
}
