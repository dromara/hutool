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

package org.dromara.hutool.core.lang.generator;

/**
 * 生成器泛型接口<br>
 * 通过实现此接口可以自定义生成对象的策略
 *
 * @param <T> 生成对象类型
 * @since 5.4.3
 */
public interface Generator<T> {

	/**
	 * 生成新的对象
	 *
	 * @return 新的对象
	 */
	T next();
}
