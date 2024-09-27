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

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.PropDesc;
import org.dromara.hutool.core.lang.copier.Copier;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONFactory;
import org.dromara.hutool.json.JSONObject;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Bean转JSON对象复制器
 *
 * @author looly
 * @since 6.0.0
 */
public class BeanToJSONCopier implements Copier<JSONObject> {

	private final Object source;
	private final JSONObject target;
	private final JSONFactory factory;

	/**
	 * 构造
	 *
	 * @param source  源对象
	 * @param target  目标JSON
	 * @param factory JSON工厂
	 */
	public BeanToJSONCopier(final Object source, final JSONObject target, final JSONFactory factory) {
		this.source = source;
		this.target = target;
		this.factory = factory;
	}

	@Override
	public JSONObject copy() {
		final JSONConfig config = factory.getConfig();
		final Map<String, PropDesc> sourcePropDescMap = BeanUtil.getBeanDesc(source.getClass())
			.getPropMap(config.isIgnoreCase());

		sourcePropDescMap.forEach((sFieldName, sDesc) -> {
			if (null == sFieldName || !sDesc.isReadable(config.isTransientSupport())) {
				// 字段空或不可读，跳过
				return;
			}

			final Object sValue = sDesc.getValue(this.source, config.isIgnoreError());
			putValue(sFieldName, sValue, config.isIgnoreNullValue());
		});

		return target;
	}

	/**
	 * 赋值，过滤则跳过
	 *
	 * @param fieldName       字段名
	 * @param sValue          源值
	 * @param ignoreNullValue 是否忽略null值
	 */
	private void putValue(final String fieldName, final Object sValue, final boolean ignoreNullValue) {
		final Predicate<MutableEntry<Object, Object>> predicate = factory.getPredicate();
		if (null != predicate) {
			final MutableEntry<Object, Object> entry = new MutableEntry<>(fieldName, sValue);
			if (predicate.test(entry)) {
				// 使用修改后的键值对
				target.putObj((String) entry.getKey(), entry.getValue());
			}
		} else if (null != sValue || !ignoreNullValue) {
			target.putObj(fieldName, sValue);
		}
	}
}
