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

package org.dromara.hutool.map.multi;

import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.lang.Opt;
import org.dromara.hutool.lang.func.SerConsumer3;
import org.dromara.hutool.map.MapUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 表格数据结构定义<br>
 * 此结构类似于Guava的Table接口，使用两个键映射到一个值，类似于表格结构。
 *
 * @param <R> 行键类型
 * @param <C> 列键类型
 * @param <V> 值类型
 * @since 5.7.23
 */
public interface Table<R, C, V> extends Iterable<Table.Cell<R, C, V>> {

	/**
	 * 是否包含指定行列的映射<br>
	 * 行和列任意一个不存在都会返回{@code false}，如果行和列都存在，值为{@code null}，也会返回{@code true}
	 *
	 * @param rowKey    行键
	 * @param columnKey 列键
	 * @return 是否包含映射
	 */
	default boolean contains(final R rowKey, final C columnKey) {
		return Opt.ofNullable(getRow(rowKey)).map((map) -> map.containsKey(columnKey)).get();
	}

	//region Row

	/**
	 * 行是否存在
	 *
	 * @param rowKey 行键
	 * @return 行是否存在
	 */
	default boolean containsRow(final R rowKey) {
		return Opt.ofNullable(rowMap()).map((map) -> map.containsKey(rowKey)).get();
	}

	/**
	 * 获取行
	 *
	 * @param rowKey 行键
	 * @return 行映射，返回的键为列键，值为表格的值
	 */
	default Map<C, V> getRow(final R rowKey) {
		return Opt.ofNullable(rowMap()).map((map) -> map.get(rowKey)).get();
	}

	/**
	 * 返回所有行的key，行的key不可重复
	 *
	 * @return 行键
	 */
	default Set<R> rowKeySet() {
		return Opt.ofNullable(rowMap()).map(Map::keySet).get();
	}

	/**
	 * 返回行列对应的Map
	 *
	 * @return map，键为行键，值为列和值的对应map
	 */
	Map<R, Map<C, V>> rowMap();
	//endregion

	//region Column

	/**
	 * 列是否存在
	 *
	 * @param columnKey 列键
	 * @return 列是否存在
	 */
	default boolean containsColumn(final C columnKey) {
		return Opt.ofNullable(columnMap()).map((map) -> map.containsKey(columnKey)).get();
	}

	/**
	 * 获取列
	 *
	 * @param columnKey 列键
	 * @return 列映射，返回的键为行键，值为表格的值
	 */
	default Map<R, V> getColumn(final C columnKey) {
		return Opt.ofNullable(columnMap()).map((map) -> map.get(columnKey)).get();
	}

	/**
	 * 返回所有列的key，列的key不可重复
	 *
	 * @return 列set
	 */
	default Set<C> columnKeySet() {
		return Opt.ofNullable(columnMap()).map(Map::keySet).get();
	}

	/**
	 * 返回所有列的key，列的key如果实现Map是可重复key，则返回对应不去重的List。
	 *
	 * @return 列set
	 * @since 5.8.0
	 */
	default List<C> columnKeys() {
		final Map<C, Map<R, V>> columnMap = columnMap();
		if(MapUtil.isEmpty(columnMap)){
			return ListUtil.empty();
		}

		final List<C> result = new ArrayList<>(columnMap.size());
		for (final Map.Entry<C, Map<R, V>> cMapEntry : columnMap.entrySet()) {
			result.add(cMapEntry.getKey());
		}
		return result;
	}

	/**
	 * 返回列-行对应的map
	 *
	 * @return map，键为列键，值为行和值的对应map
	 */
	Map<C, Map<R, V>> columnMap();
	//endregion

	//region value

	/**
	 * 指定值是否存在
	 *
	 * @param value 值
	 * @return 值
	 */
	default boolean containsValue(final V value){
		final Collection<Map<C, V>> rows = Opt.ofNullable(rowMap()).map(Map::values).get();
		if(null != rows){
			for (final Map<C, V> row : rows) {
				if (row.containsValue(value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取指定值
	 *
	 * @param rowKey    行键
	 * @param columnKey 列键
	 * @return 值，如果值不存在，返回{@code null}
	 */
	default V get(final R rowKey, final C columnKey) {
		return Opt.ofNullable(getRow(rowKey)).map((map) -> map.get(columnKey)).get();
	}

	/**
	 * 所有行列值的集合
	 *
	 * @return 值的集合
	 */
	Collection<V> values();
	//endregion

	/**
	 * 所有单元格集合
	 *
	 * @return 单元格集合
	 */
	Set<Cell<R, C, V>> cellSet();

	/**
	 * 为表格指定行列赋值，如果不存在，创建之，存在则替换之，返回原值
	 *
	 * @param rowKey    行键
	 * @param columnKey 列键
	 * @param value     值
	 * @return 原值，不存在返回{@code null}
	 */
	V put(R rowKey, C columnKey, V value);

	/**
	 * 批量加入
	 *
	 * @param table 其他table
	 */
	default void putAll(final Table<? extends R, ? extends C, ? extends V> table){
		if (null != table) {
			for (final Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
				put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
			}
		}
	}

	/**
	 * 移除指定值
	 *
	 * @param rowKey    行键
	 * @param columnKey 列键
	 * @return 移除的值，如果值不存在，返回{@code null}
	 */
	V remove(R rowKey, C columnKey);

	/**
	 * 表格是否为空
	 *
	 * @return 是否为空
	 */
	boolean isEmpty();

	/**
	 * 表格大小，一般为单元格的个数
	 *
	 * @return 表格大小
	 */
	default int size(){
		final Map<R, Map<C, V>> rowMap = rowMap();
		if(MapUtil.isEmpty(rowMap)){
			return 0;
		}
		int size = 0;
		for (final Map<C, V> map : rowMap.values()) {
			size += map.size();
		}
		return size;
	}

	/**
	 * 清空表格
	 */
	void clear();

	/**
	 * 遍历表格的单元格，处理值
	 *
	 * @param consumer 单元格值处理器
	 */
	default void forEach(final SerConsumer3<? super R, ? super C, ? super V> consumer) {
		for (final Cell<R, C, V> cell : this) {
			consumer.accept(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
		}
	}

	/**
	 * 单元格，用于表示一个单元格的行、列和值
	 *
	 * @param <R> 行键类型
	 * @param <C> 列键类型
	 * @param <V> 值类型
	 */
	interface Cell<R, C, V> {
		/**
		 * 获取行键
		 *
		 * @return 行键
		 */
		R getRowKey();

		/**
		 * 获取列键
		 *
		 * @return 列键
		 */
		C getColumnKey();

		/**
		 * 获取值
		 *
		 * @return 值
		 */
		V getValue();
	}
}
