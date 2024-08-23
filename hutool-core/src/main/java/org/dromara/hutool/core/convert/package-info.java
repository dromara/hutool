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
 * 万能类型转换器以及各种类型转换的实现类，其中Convert为转换器入口，提供各种toXXX方法和convert方法<br>
 * 转换器是典型的策略模式应用，可自定义转换策略。Hutool提供了常用类型的转换策略，自定义转换接口包括：
 * <ul>
 *     <li>{@link org.dromara.hutool.core.convert.Converter}，标准转换接口，通过类型匹配策略后调用使用。</li>
 *     <li>{@link org.dromara.hutool.core.convert.MatcherConverter}，带有match方法的Converter，通过自身匹配判断调用转换。</li>
 * </ul>
 *
 * 公共的转换器封装有两种：
 * <ul>
 *     <li>{@link org.dromara.hutool.core.convert.RegisterConverter}，提供预定义的转换规则，也可以注册自定义转换规则。</li>
 *     <li>{@link org.dromara.hutool.core.convert.CompositeConverter}，复合转换器，封装基于注册的、特别转换（泛型转换）等规则，实现万能转换。</li>
 * </ul>
 *
 * @author looly
 *
 */
package org.dromara.hutool.core.convert;
