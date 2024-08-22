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

package org.dromara.hutool.json.engine;

import java.io.Serializable;

/**
 * JSON引擎配置
 *
 * @author Looly
 * @since 6.0.0
 */
public class JSONEngineConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建JSON引擎配置
	 *
	 * @return JSONEngineConfig
	 */
	public static JSONEngineConfig of() {
		return new JSONEngineConfig();
	}

	/**
	 * 是否格式化输出
	 */
	private boolean prettyPrint;

	/**
	 * 获取是否启用格式化输出
	 *
	 * @return true如果配置了格式化输出，否则返回false
	 */
	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	/**
	 * 设置是否启用格式化输出
	 *
	 * @param prettyPrint 布尔值，指示是否启用格式化输出
	 * @return this
	 */
	public JSONEngineConfig setPrettyPrint(final boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
		return this;
	}
}
