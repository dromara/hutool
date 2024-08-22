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
 * 多参数类型的Map实现，包括集合类型值的MultiValueMap和Table<br>
 * <ul>
 *     <li>MultiValueMap：一个键对应多个值的集合的实现，类似于树的结构。</li>
 *     <li>Table：使用两个键映射到一个值，类似于表格结构。</li>
 * </ul>
 *
 * <pre>
 *                   MultiValueMap
 *                         |
 *                   AbsCollValueMap
 *                         ||
 *   [CollectionValueMap, SetValueMap, ListValueMap]
 * </pre>
 * <pre>
 *                       Table
 *                         |
 *                      AbsTable
 *                         ||
 *                    [RowKeyTable]
 * </pre>
 *
 * @author looly
 *
 */
package org.dromara.hutool.core.map.multi;
