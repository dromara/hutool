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

package org.dromara.hutool.core.lang.ref;

/**
 * 针对{@link java.lang.ref.Reference}的接口定义，用于扩展功能<br>
 * 例如提供自定义的无需回收对象
 *
 * @param <T> 对象类型
 */
@FunctionalInterface
public interface Ref<T> {

	/**
	 * 获取引用的原始对象
	 *
	 * @return 原始对象
	 */
	T get();
}
