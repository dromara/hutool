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

package org.dromara.hutool.json.serialize;

import org.dromara.hutool.json.JSON;

import java.lang.reflect.Type;

/**
 * JSON自定义反序列化接口，实现JSON to Bean，使用方式为：
 * <ul>
 *     <li>定义好反序列化规则，使用{@link GlobalSerializeMapping#putDeserializer(Type, JSONDeserializer)}，关联指定类型与转换器实现反序列化。</li>
 *     <li>使Bean实现此接口，调用{@link #deserialize(JSON)}解析字段，返回this即可。</li>
 * </ul>
 *
 * @param <T> 反序列化后的类型
 * @author Looly
 */
@FunctionalInterface
public interface JSONDeserializer<T> {

	/**
	 * 反序列化，通过实现此方法，自定义实现JSON转换为指定类型的逻辑
	 *
	 * @param json {@link JSON}
	 * @return 目标对象
	 */
	T deserialize(JSON json);
}
