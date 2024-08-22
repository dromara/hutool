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

package org.dromara.hutool.core.lang.selector;

/**
 * 选择器接口<br>
 * 用于抽象负载均衡策略中的选择方式
 *
 * @param <T> 选择对象类型
 * @author looly
 * @since 6.0.0
 */
@FunctionalInterface
public interface Selector<T> {

	/**
	 * 选择下一个对象
	 *
	 * @return 下一个对象
	 */
	T select();
}
