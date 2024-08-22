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

package org.dromara.hutool.core.reflect;

/**
 * Invoker接口定义了调用目标对象的方法的规范。<br>
 * 它允许动态地调用方法，增强了代码的灵活性和扩展性。<br>
 * 参考：org.apache.ibatis.reflection.invoker.Invoker
 *
 * @author MyBatis(Clinton Begin)
 */
public interface Invoker {

	/**
	 * 调用指定目标对象的方法。
	 *
	 * @param target 目标对象，调用的方法属于该对象。
	 * @param args   方法调用的参数数组。
	 * @return 方法的返回值，方法的返回类型可以是任意类型。
	 * @param <T> 返回类型
	 */
	<T> T invoke(Object target, Object... args);

	/**
	 * 获取调用方法的返回类型或参数类型。
	 *
	 * @return 调用方法的返回类型，作为Class对象返回。
	 */
	Class<?> getType();
}

