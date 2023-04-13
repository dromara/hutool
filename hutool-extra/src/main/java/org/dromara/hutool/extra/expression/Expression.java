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

package org.dromara.hutool.extra.expression;

import java.util.Map;

/**
 * 表达式定义，用于表示编译后的表达式。
 *
 * @author looly
 * @since 6.0.0
 */
public interface Expression {

	/**
	 * 执行表达式
	 *
	 * @param context 表达式上下文，用于存储表达式中所需的变量值等
	 * @return 执行结果
	 */
	Object eval(final Map<String, Object> context);
}
