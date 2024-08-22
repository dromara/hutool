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

package org.dromara.hutool.json.serialize;

import org.dromara.hutool.json.JSONObject;

/**
 * 对象的序列化接口，用于将特定对象序列化为{@link JSONObject}
 * @param <V> 对象类型
 *
 * @author Looly
 */
@FunctionalInterface
public interface JSONObjectSerializer<V> extends JSONSerializer<JSONObject, V>{}
