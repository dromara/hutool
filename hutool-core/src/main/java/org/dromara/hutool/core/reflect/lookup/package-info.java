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
 * {@link java.lang.invoke.MethodHandles.Lookup} 创建封装，以根据不同的条件查找{@link java.lang.invoke.MethodHandles}<br>
 *
 *
 * <p>
 * jdk8中如果直接调用{@link java.lang.invoke.MethodHandles#lookup()}获取到的{@link java.lang.invoke.MethodHandles.Lookup}在调用findSpecial和unreflectSpecial
 * 时会出现权限不够问题，抛出"no private access for invokespecial"异常，因此针对JDK8及JDK9+分别封装lookup方法。
 * </p>
 *
 * <p>
 * 参考：https://blog.csdn.net/u013202238/article/details/108687086
 *
 * @author looly
 * @since 6.0.0
 */
package org.dromara.hutool.core.reflect.lookup;
