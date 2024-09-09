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

package org.dromara.hutool.http.client.engine;

import org.dromara.hutool.http.client.ClientConfig;
import org.dromara.hutool.http.client.cookie.CookieStoreSpi;

/**
 * 客户端引擎抽象类，用于保存配置和定义初始化，并提供：
 * <ul>
 *     <li>{@link #reset()}用于重置客户端</li>
 *     <li>{@link #initEngine()}初始化客户端</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public abstract class AbstractClientEngine implements ClientEngine{

	protected ClientConfig config;
	protected CookieStoreSpi cookieStore;

	/**
	 * 获得Cookie存储器
	 *
	 * @return Cookie存储器
	 */
	public CookieStoreSpi getCookieStore() {
		return this.cookieStore;
	}

	@Override
	public ClientEngine init(final ClientConfig config) {
		this.config = config;
		reset();
		return this;
	}

	/**
	 * 重置引擎
	 */
	protected abstract void reset();

	/**
	 * 初始化引擎，实现逻辑中如果初始化完成，不再重新初始化
	 */
	protected abstract void initEngine();
}
