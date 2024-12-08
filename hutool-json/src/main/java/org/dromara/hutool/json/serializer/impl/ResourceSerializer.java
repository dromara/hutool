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

package org.dromara.hutool.json.serializer.impl;

import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONFactory;
import org.dromara.hutool.json.reader.JSONParser;
import org.dromara.hutool.json.reader.JSONTokener;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

/**
 * {@link Resource}序列化器
 *
 * @author looly
 * @since 6.0.0
 */
public class ResourceSerializer implements MatcherJSONSerializer<Resource> {

	/**
	 * 单例
	 */
	public static final ResourceSerializer INSTANCE = new ResourceSerializer();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof Resource;
	}

	@Override
	public JSON serialize(final Resource bean, final JSONContext context) {
		return context.getFactory().ofParser(new JSONTokener(bean.getStream(), context.config().isIgnoreZeroWithChar())).parse();
	}

	/**
	 * 从{@link JSONTokener} 中读取JSON字符串，并转换为JSON
	 *
	 * @param tokener {@link JSONTokener}
	 * @return JSON
	 */
	private JSON mapFromTokener(final JSONTokener tokener, final JSONFactory factory) {
		return JSONParser.of(tokener, factory).parse();
	}
}
