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
 * 提供合成接口，共同提供{@link Iterable}和{@link Iterator}功能
 *
 * @param <T> 节点类型
 * @author looly
 * @since 5.7.14
 */
public interface IterableIter<T> extends Iterable<T>, Iterator<T> {

	@Override
	default Iterator<T> iterator() {
		return this;
	}

}
