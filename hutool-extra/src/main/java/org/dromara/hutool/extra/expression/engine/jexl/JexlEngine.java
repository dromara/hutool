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

package org.dromara.hutool.extra.expression.engine.jexl;

import org.apache.commons.jexl3.JexlBuilder;
import org.dromara.hutool.core.func.SimpleWrapper;
import org.dromara.hutool.extra.expression.Expression;
import org.dromara.hutool.extra.expression.ExpressionEngine;

/**
 * Jexl3引擎封装<br>
 * 见：https://github.com/apache/commons-jexl
 *
 * @since 5.5.0
 * @author looly
 */
public class JexlEngine extends SimpleWrapper<org.apache.commons.jexl3.JexlEngine>
	implements ExpressionEngine {

	/**
	 * 构造
	 */
	public JexlEngine(){
		super(
			(new JexlBuilder())
			.cache(512)
			.strict(true)
			.silent(false)
			.create()
		);
	}

	@Override
	public Expression compile(final String expression) {
		try{
			return new JexlExpression(this.raw.createExpression(expression));
		} catch (final Exception ignore){
			// https://gitee.com/dromara/hutool/issues/I4B70D
			// 支持脚本
			return new JexlScript(this.raw.createScript(expression));
		}
	}
}
