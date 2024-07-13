/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.func;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 断言与一元操作符的组合<br>
 * 此接口用于组合断言和操作，如果{@link #test(Object)}为{@code true}，则执行{@link #apply(Object)}，否则不执行
 *
 * @param <T> 被操作对象类型
 * @author Looly
 * @since 6.0.0
 */
public interface PredicateUnaryOperator<T> extends Predicate<T>, UnaryOperator<T> {
}
