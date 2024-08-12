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

package org.dromara.hutool.core.lang.caller;

/**
 * 调用者接口<br>
 * 可以通过此接口的实现类方法获取调用者、多级调用者以及判断是否被调用
 *
 * @author Looly
 *
 */
public interface Caller {
	/**
	 * 获得调用者
	 *
	 * @return 调用者
	 */
	Class<?> getCaller();

	/**
	 * 获得调用者的调用者
	 *
	 * @return 调用者的调用者
	 */
	Class<?> getCallerCaller();

	/**
	 * 获得调用者，指定第几级调用者 调用者层级关系：
	 *
	 * <pre>
	 * 0 {@link CallerUtil}
	 * 1 调用{@link CallerUtil}中方法的类
	 * 2 调用者的调用者
	 * ...
	 * </pre>
	 *
	 * @param depth 层级。0表示{@link CallerUtil}本身，1表示调用{@link CallerUtil}的类，2表示调用者的调用者，依次类推
	 * @return 第几级调用者
	 */
	Class<?> getCaller(int depth);

	/**
	 * 是否被指定类调用
	 *
	 * @param clazz 调用者类
	 * @return 是否被调用
	 */
	boolean isCalledBy(Class<?> clazz);
}
