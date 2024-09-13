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

package org.dromara.hutool.json.serializer;

import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.OldJSONObject;

/**
 * 序列化接口，通过实现此接口，实现自定义的对象转换为JSON的操作
 *
 * @param <V> 对象类型
 * @author Looly
 */
@FunctionalInterface
public interface JSONSerializer<V> {

	/**
	 * 序列化实现，通过实现此方法，将指定类型的对象转换为{@link JSON}对象,可以：
	 * <ul>
	 *     <li>如果为原始类型，可以转为{@link org.dromara.hutool.json.JSONPrimitive}</li>
	 *     <li>如果是集合或数组类，可以转为{@link org.dromara.hutool.json.JSONArray}</li>
	 *     <li>如果是Bean或键值对类型，可以转为{@link OldJSONObject}</li>
	 * </ul>
	 *
	 * @param bean    指定类型对象
	 * @param context JSON上下文，用于获取当前json节点或配置等信息
	 * @return JSON
	 */
	JSON serialize(V bean, JSONContext context);
}
