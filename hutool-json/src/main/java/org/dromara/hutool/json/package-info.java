/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
