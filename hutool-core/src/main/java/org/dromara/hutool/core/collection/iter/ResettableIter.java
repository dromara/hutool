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

package org.dromara.hutool.core.collection.iter;

import java.util.Iterator;

/**
 * 支持重置的{@link Iterator} 接口<br>
 * 通过实现{@link #reset()}，重置此{@link Iterator}后可实现复用重新遍历
 *
 * @param <E> 元素类型
 * @since 5.8.0
 */
public interface ResettableIter<E> extends Iterator<E> {

	/**
	 * 重置，重置后可重新遍历
	 */
	void reset();
}
