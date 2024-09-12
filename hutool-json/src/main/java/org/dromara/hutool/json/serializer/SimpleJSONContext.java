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

package org.dromara.hutool.json.serializer;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;

/**
 * 简单的JSON上下文，用于在JSON序列化时提供配置项
 *
 * @author looly
 * @since 6.0.0
 */
public class SimpleJSONContext implements JSONContext {

	private final JSON json;
	private final JSONConfig config;

	/**
	 * 构造
	 *
	 * @param json JSON对象
	 */
	public SimpleJSONContext(final JSON json) {
		this(Assert.notNull(json), json.config());
	}

	/**
	 * 构造
	 *
	 * @param json   JSON对象
	 * @param config 配置项
	 */
	public SimpleJSONContext(final JSON json, final JSONConfig config) {
		this.json = json;
		this.config = config;
	}

	@Override
	public JSON getContextJson() {
		return this.json;
	}

	@Override
	public JSONConfig config() {
		return this.config;
	}
}
