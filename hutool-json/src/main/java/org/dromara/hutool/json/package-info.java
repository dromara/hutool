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
 * JSON（JavaScript Object Notation JavaScript对象表示法）封装，包含以下组件：
 * <ul>
 *     <li>JSONObject：使用键值对表示的数据类型，使用"{}"包围</li>
 *     <li>JSONArray：使用列表表示的数据类型，使用"[]"包围</li>
 * </ul>
 * JSON封装主要包括JSON表示和JSON转换：
 *
 * <pre>{@code
 *               <--JSONConverter--              <---JSONParser----
 *     Java对象  <=================>   JSON对象   <=================>    JSON字符串
 *               ------mapper----->              ---JSONWriter---->
 * }</pre>
 *
 * 当然，为了高效转换，如果没有自定义需求，Java对象可以不通过JSON对象与JSON字符串转换：
 * <ul>
 *     <li>JSONTokener：JSON字符串底层解析器，通过Stream方式读取JSON字符串并对不同字段自定义处理。</li>
 *     <li>JSONWriter：JSON字符串底层生成器，可以自定义写出任意对象。</li>
 * </ul>
 * <pre>{@code
 *                <---JSONTokener----
 *     Java对象    <=================>    JSON字符串
 *                ---JSONWriter---->
 * }</pre>
 *
 * @author looly
 *
 */
package org.dromara.hutool.json;
