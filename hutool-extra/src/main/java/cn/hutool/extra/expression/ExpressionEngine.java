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

package cn.hutool.extra.expression;

import java.util.Map;

/**
 * 表达式引擎API接口，通过实现此接口，完成表达式的解析和执行
 *
 * @author looll,independenter
 * @since 5.5.0
 */
public interface ExpressionEngine {

	/**
	 * 执行表达式
	 * @param expression 表达式
	 * @param context 表达式上下文，用于存储表达式中所需的变量值等
	 * @return 执行结果
	 */
	Object eval(String expression, Map<String, Object> context);
}
