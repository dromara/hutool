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

import org.dromara.hutool.json.JSON;

import java.lang.reflect.Type;

/**
 * 带有匹配方法的 JSON反序列化器<br>
 * 匹配方法返回true表示匹配，反序列化器将执行反序列化操作
 *
 * @param <V> 反序列化结果类型
 * @author looly
 * @since 6.0.0
 */
public interface MatcherJSONDeserializer<V> extends JSONDeserializer<V> {

	/**
	 * 匹配反序列化器是否匹配
	 *
	 * @param json            JSON对象
	 * @param deserializeType 反序列化类型
	 * @return 是否匹配
	 */
	boolean match(JSON json, Type deserializeType);
}
