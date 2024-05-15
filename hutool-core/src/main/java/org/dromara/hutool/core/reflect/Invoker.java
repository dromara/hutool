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

