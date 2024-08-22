/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

/**
 * 万能类型转换器以及各种类型转换的实现类，其中Convert为转换器入口，提供各种toXXX方法和convert方法
 *
 * <p>
 * 转换器是典型的策略模式应用，通过实现{@link org.dromara.hutool.core.convert.Converter} 接口，
 * 自定义转换策略。Hutool提供了常用类型的转换策略。
 * </p>
 *
 * @author looly
 *
 */
package org.dromara.hutool.core.convert;
