/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
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
 *     Java对象  <----------------->   JSON对象   <----------------->    JSON字符串
 *               ------mapper----->              ---JSONWriter---->
 * }</pre>
 *
 *
 * @author looly
 *
 */
package org.dromara.hutool.json;
