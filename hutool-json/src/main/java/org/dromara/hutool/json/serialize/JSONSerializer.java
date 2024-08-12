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

/**
 * 序列化接口，通过实现此接口，实现自定义的对象转换为JSON的操作
 *
 * @param <T> JSON类型，可以是JSONObject或者JSONArray
 * @param <V> 对象类型
 * @author Looly
 */
@FunctionalInterface
public interface JSONSerializer<T extends JSON, V> {

	/**
	 * 序列化实现，通过实现此方法，将指定类型的对象转换为{@link JSON}对象<br>
	 * 转换后的对象可以为JSONObject也可以为JSONArray，首先new一个空的JSON，然后将需要的数据字段put到JSON对象中去即可。
	 *
	 * @param json JSON，可以为JSONObject或者JSONArray
	 * @param bean 指定类型对象
	 */
	void serialize(T json, V bean);
}
