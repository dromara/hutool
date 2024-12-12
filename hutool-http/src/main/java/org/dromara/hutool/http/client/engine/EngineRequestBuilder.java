/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.http.client.engine;

import org.dromara.hutool.http.client.Request;

/**
 * 引擎的请求对象构建器<br》
 * 用于将{@link Request}转换为引擎对应的请求对象
 *
 * @param <T> 引擎请求对象类型
 */
public interface EngineRequestBuilder<T> {

	/**
	 * 构建引擎请求对象
	 *
	 * @param message 请求对象
	 * @return 引擎请求对象
	 */
	T build(Request message);
}
