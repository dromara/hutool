/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
