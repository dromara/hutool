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

package org.dromara.hutool.core.codec.hash;

import org.dromara.hutool.core.codec.Encoder;
import org.dromara.hutool.core.codec.Number128;

/**
 * Hash计算接口
 *
 * @param <T> 被计算hash的对象类型
 * @author looly
 * @since 5.2.5
 */
@FunctionalInterface
public interface Hash128<T> extends Encoder<T, Number> {

	/**
	 * 计算Hash值
	 *
	 * @param t 对象
	 * @return hash
	 */
	Number128 hash128(T t);

	@Override
	default Number encode(final T t){
		return hash128(t);
	}
}
