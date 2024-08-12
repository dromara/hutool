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

package org.dromara.hutool.json.engine;

/**
 * JSONEngine抽象类，用于保存配置和定义初始化，并提供：
 * <ul>
 *     <li>{@link #reset()}用于重置引擎</li>
 *     <li>{@link #initEngine()}初始化引擎</li>
 * </ul>
 *
 * @author Looly
 * @since 6.0.0
 */
public abstract class AbstractJSONEngine implements JSONEngine {

	/**
	 * JSON引擎配置，{@code null}表示默认配置
	 */
	protected JSONEngineConfig config;

	@Override
	public JSONEngine init(final JSONEngineConfig config) {
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
