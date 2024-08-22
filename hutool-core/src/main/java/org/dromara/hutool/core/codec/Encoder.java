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

package org.dromara.hutool.core.codec;

/**
 * 编码接口
 *
 * @param <T> 被编码的数据类型
 * @param <R> 编码后的数据类型
 * @author looly
 * @since 5.7.22
 */
public interface Encoder<T, R> {

	/**
	 * 执行编码
	 *
	 * @param data 被编码的数据
	 * @return 编码后的数据
	 */
	R encode(T data);
}
