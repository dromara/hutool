/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
package org.dromara.hutool.map.multi;
