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

package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.map.MapWrapper;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.writer.JSONWriter;

public class JSONObject extends MapWrapper<String, JSON> implements JSON{

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_CAPACITY = MapUtil.DEFAULT_INITIAL_CAPACITY;

	/**
	 * 配置项
	 */
	private JSONConfig config;

	/**
	 * 构造，初始容量为 {@link #DEFAULT_CAPACITY}，KEY有序
	 */
	public JSONObject() {
		this(JSONConfig.of());
	}

	/**
	 * 构造
	 *
	 * @param config JSON配置项
	 * @since 4.6.5
	 */
	public JSONObject(final JSONConfig config) {
		this(DEFAULT_CAPACITY, config);
	}

	/**
	 * 构造
	 *
	 * @param capacity 初始大小
	 * @param config   JSON配置项，{@code null}则使用默认配置
	 */
	public JSONObject(final int capacity, final JSONConfig config) {
		super(InternalJSONUtil.createRawMap(capacity, config));
		this.config = ObjUtil.defaultIfNull(config, JSONConfig::of);
	}

	@Override
	public JSONConfig config() {
		return this.config;
	}

	@Override
	public void write(final JSONWriter writer) throws JSONException {
		writer.beginObj();
		this.forEach((key, value) -> writer.writeField(new MutableEntry<>(key, value)));
		writer.end();
	}
}
