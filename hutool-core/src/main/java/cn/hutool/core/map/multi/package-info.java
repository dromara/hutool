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
package cn.hutool.core.map.multi;
