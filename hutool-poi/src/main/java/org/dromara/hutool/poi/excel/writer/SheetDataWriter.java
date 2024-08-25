/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.poi.excel.writer;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;
import org.dromara.hutool.core.map.multi.Table;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.poi.excel.RowUtil;
import org.dromara.hutool.poi.excel.cell.CellUtil;
import org.dromara.hutool.poi.excel.cell.editors.CellEditor;
import org.dromara.hutool.poi.excel.style.StyleSet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sheet数据写出器<br>
 * 此对象只封装将数据写出到Sheet中，并不刷新到文件
 *
 * @author looly
 * @since 6.0.0
 */
public class SheetDataWriter {

	private final Sheet sheet;
	private final ExcelWriteConfig config;
	private StyleSet styleSet;

	/**
	 * 标题项对应列号缓存，每次写标题更新此缓存<br>
	 * 此缓存用于用户多次write时，寻找标题位置
	 */
	private Map<String, Integer> headLocationCache;
	/**
	 * 当前行，用于标记初始可写数据的行和部分写完后当前的行
	 */
	private final AtomicInteger currentRow;

	/**
	 * 构造
	 *
	 * @param sheet    {@link Sheet}
	 * @param config   Excel配置
	 * @param styleSet 样式表
	 */
	public SheetDataWriter(final Sheet sheet, final ExcelWriteConfig config, final StyleSet styleSet) {
		this.sheet = sheet;
		this.config = config;
		this.styleSet = styleSet;
		this.currentRow = new AtomicInteger(0);
	}

	/**
	 * 设置样式表
	 * @param styleSet 样式表
	 * @return this
	 */
	public SheetDataWriter setStyleSet(final StyleSet styleSet) {
		this.styleSet = styleSet;
		return this;
	}

	/**
	 * 写出一行，根据rowBean数据类型不同，写出情况如下：
	 *
	 * <pre>
	 * 1、如果为Iterable，直接写出一行
	 * 2、如果为Map，isWriteKeyAsHead为true写出两行，Map的keys做为一行，values做为第二行，否则只写出一行values
	 * 3、如果为Bean，转为Map写出，isWriteKeyAsHead为true写出两行，Map的keys做为一行，values做为第二行，否则只写出一行values
	 * </pre>
	 *
	 * @param rowBean          写出的Bean
	 * @param isWriteKeyAsHead 为true写出两行，Map的keys做为一行，values做为第二行，否则只写出一行values
	 * @return this
	 * @see #writeRow(Iterable)
	 * @see #writeRow(Map, boolean)
	 * @since 4.1.5
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public SheetDataWriter writeRow(final Object rowBean, final boolean isWriteKeyAsHead) {
		final ExcelWriteConfig config = this.config;

		final Map rowMap;
		if (rowBean instanceof Map) {
			if (MapUtil.isNotEmpty(config.getHeaderAlias())) {
				rowMap = MapUtil.newTreeMap((Map) rowBean, config.getCachedAliasComparator());
			} else {
				rowMap = (Map) rowBean;
			}
		} else if (rowBean instanceof Iterable) {
			// issue#2398@Github
			// MapWrapper由于实现了Iterable接口，应该优先按照Map处理
			return writeRow((Iterable<?>) rowBean);
		} else if (rowBean instanceof Hyperlink) {
			// Hyperlink当成一个值
			return writeRow(ListUtil.of(rowBean), isWriteKeyAsHead);
		} else if (BeanUtil.isReadableBean(rowBean.getClass())) {
			if (MapUtil.isEmpty(config.getHeaderAlias())) {
				rowMap = BeanUtil.beanToMap(rowBean, new LinkedHashMap<>(), false, false);
			} else {
				// 别名存在情况下按照别名的添加顺序排序Bean数据
				rowMap = BeanUtil.beanToMap(rowBean, new TreeMap<>(config.getCachedAliasComparator()), false, false);
			}
		} else {
			// 其它转为字符串默认输出
			return writeRow(ListUtil.of(rowBean), isWriteKeyAsHead);
		}
		return writeRow(rowMap, isWriteKeyAsHead);
	}

	/**
	 * 将一个Map写入到Excel，isWriteKeyAsHead为true写出两行，Map的keys做为一行，values做为第二行，否则只写出一行values<br>
	 * 如果rowMap为空（包括null），则写出空行
	 *
	 * @param rowMap           写出的Map，为空（包括null），则写出空行
	 * @param isWriteKeyAsHead 为true写出两行，Map的keys做为一行，values做为第二行，否则只写出一行values
	 * @return this
	 */
	public SheetDataWriter writeRow(final Map<?, ?> rowMap, final boolean isWriteKeyAsHead) {
		if (MapUtil.isEmpty(rowMap)) {
			// 如果写出数据为null或空，跳过当前行
			passAndGet();
			return this;
		}

		final Table<?, ?, ?> aliasTable = this.config.aliasTable(rowMap);
		if (isWriteKeyAsHead) {
			// 写出标题行，并记录标题别名和列号的关系
			writeHeadRow(aliasTable.columnKeys());
			// 记录原数据key和别名对应列号
			int i = 0;
			for (final Object key : aliasTable.rowKeySet()) {
				this.headLocationCache.putIfAbsent(StrUtil.toString(key), i);
				i++;
			}
		}

		// 如果已经写出标题行，根据标题行找对应的值写入
		if (MapUtil.isNotEmpty(this.headLocationCache)) {
			final Row row = RowUtil.getOrCreateRow(this.sheet, this.currentRow.getAndIncrement());
			final CellEditor cellEditor = this.config.getCellEditor();
			Integer columnIndex;
			for (final Table.Cell<?, ?, ?> cell : aliasTable) {
				columnIndex = getColumnIndex(cell);
				if (null != columnIndex) {
					CellUtil.setCellValue(CellUtil.getOrCreateCell(row, columnIndex), cell.getValue(), this.styleSet, false, cellEditor);
				}
			}
		} else {
			writeRow(aliasTable.values());
		}
		return this;
	}

	/**
	 * 写出一行标题数据<br>
	 * 本方法只是将数据写入Workbook中的Sheet，并不写出到文件<br>
	 * 写出的起始行为当前行号，可使用{@link #getCurrentRow()}方法调用，根据写出的的行数，当前行号自动+1
	 *
	 * @param rowData 一行的数据
	 * @return this
	 */
	public SheetDataWriter writeHeadRow(final Iterable<?> rowData) {
		this.headLocationCache = new SafeConcurrentHashMap<>();

		final int rowNum = this.currentRow.getAndIncrement();
		final Row row = this.config.insertRow ? this.sheet.createRow(rowNum) : RowUtil.getOrCreateRow(this.sheet, rowNum);

		final CellEditor cellEditor = this.config.getCellEditor();
		int i = 0;
		Cell cell;
		for (final Object value : rowData) {
			cell = CellUtil.getOrCreateCell(row, i);
			CellUtil.setCellValue(cell, value, this.styleSet, true, cellEditor);
			this.headLocationCache.put(StrUtil.toString(value), i);
			i++;
		}
		return this;
	}

	/**
	 * 写出一行数据<br>
	 * 本方法只是将数据写入Workbook中的Sheet，并不写出到文件<br>
	 * 写出的起始行为当前行号，可使用{@link #getCurrentRow()}方法调用，根据写出的的行数，当前行号自动+1
	 *
	 * @param rowData 一行的数据
	 * @return this
	 */
	public SheetDataWriter writeRow(final Iterable<?> rowData) {
		final int rowNum = this.currentRow.getAndIncrement();
		final Row row = this.config.insertRow ? this.sheet.createRow(rowNum) : RowUtil.getOrCreateRow(this.sheet, rowNum);
		RowUtil.writeRow(row, rowData, this.styleSet, false, this.config.getCellEditor());
		return this;
	}

	// region ----- currentRow ops
	/**
	 * 获得当前行
	 *
	 * @return 当前行
	 */
	public int getCurrentRow() {
		return this.currentRow.get();
	}

	/**
	 * 设置当前所在行
	 *
	 * @param rowIndex 行号
	 * @return this
	 */
	public SheetDataWriter setCurrentRow(final int rowIndex) {
		this.currentRow.set(rowIndex);
		return this;
	}

	/**
	 * 跳过当前行，并获取下一行的行号
	 *
	 * @return this
	 */
	public int passAndGet() {
		return this.currentRow.incrementAndGet();
	}

	/**
	 * 跳过指定行数，并获取当前行号
	 *
	 * @param rowNum 跳过的行数
	 * @return this
	 */
	public int passRowsAndGet(final int rowNum) {
		return this.currentRow.addAndGet(rowNum);
	}

	/**
	 * 重置当前行为0
	 *
	 * @return this
	 */
	public SheetDataWriter resetRow() {
		this.currentRow.set(0);
		return this;
	}
	// endregion

	/**
	 * 查找标题或标题别名对应的列号
	 *
	 * @param cell 别名表，rowKey：原名，columnKey：别名
	 * @return 列号，如果未找到返回null
	 */
	private Integer getColumnIndex(final Table.Cell<?, ?, ?> cell) {
		// 首先查找原名对应的列号
		Integer location = this.headLocationCache.get(StrUtil.toString(cell.getRowKey()));
		if (null == location) {
			// 未找到，则查找别名对应的列号
			location = this.headLocationCache.get(StrUtil.toString(cell.getColumnKey()));
		}
		return location;
	}
}
