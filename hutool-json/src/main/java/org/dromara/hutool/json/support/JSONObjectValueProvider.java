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

package org.dromara.hutool.json.support;

import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONObject;

import java.lang.reflect.Type;

/**
 * JSONObject值提供者，用于将JSONObject中的值注入Bean<br>
 * 兼容下划线模式的JSON转换为驼峰模式
 *
 * @author Looly
 * @since 6.0.0
 */
public class JSONObjectValueProvider implements ValueProvider<String> {

	private final JSONObject jsonObject;

	/**
	 * 构造
	 *
	 * @param jsonObject JSON对象
	 */
	public JSONObjectValueProvider(final JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public boolean containsKey(final String key) {
		return jsonObject.containsKey(key) || jsonObject.containsKey(StrUtil.toUnderlineCase(key));
	}

	@Override
	public Object value(final String key, final Type valueType) {
		JSON value = jsonObject.get(key);
		if (null == value) {
			value = jsonObject.get(StrUtil.toUnderlineCase(key));
			if(null == value){
				return null;
			}
		}
		return value.toBean(valueType);
	}
}
