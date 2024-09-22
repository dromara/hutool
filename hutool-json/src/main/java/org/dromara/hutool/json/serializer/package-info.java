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

/**
 * JSON序列化和反序列化，提供对象和JSON之间的转换，我们定义：
 * 1. 对象定义：
 * <ul>
 *     <li>Java对象，可以是POJO（Java Bean），也可以是String、int、Map，List等。</li>
 *     <li>JSON对象，只包含JSON接口实现：JSONObject、JSONArray、JSONPrimitive和{@code null}。</li>
 * </ul>
 *
 * 2. 序列化定义：
 * <ul>
 *     <li>序列化（Serialize）    指：【Java对象】 转换为 【JSON对象】</li>
 *     <li>反序列化（Deserialize）指：【JSON对象】 转换为 【Java对象】</li>
 * </ul>
 *
 * 3. JSON序列化实现：
 * <ul>
 *     <li>TypeAdapter：类型适配器，标记序列化或反序列化</li>
 *     <li>JSONSerializer：JSON序列化接口，用于自定义序列化</li>
 *     <li>JSONDeserializer：JSON反序列化接口，用于自定义反序列化</li>
 * </ul>
 *
 * 4. JSON序列化管理：<br>
 * TypeAdapterManager用于管理定义的序列化和反序列化器
 *
 */
package org.dromara.hutool.json.serializer;
