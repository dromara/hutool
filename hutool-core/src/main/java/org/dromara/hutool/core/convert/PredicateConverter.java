/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.convert;

import java.lang.reflect.Type;
import java.util.function.Predicate;

/**
 * 断言转换器<br>
 * 判断目标对象是否满足断言，满足则转换，否则跳过<br>
 * 实现此接口同样可以不判断断言而直接转换
 *
 * @author Looly
 * @since 6.0.0
 */
public interface PredicateConverter extends Converter, Predicate<Type> {
}
