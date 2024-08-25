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

package org.dromara.hutool.poi.excel.writer;

import org.dromara.hutool.core.comparator.IndexedComparator;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.map.TableMap;
import org.dromara.hutool.core.map.multi.RowKeyTable;
import org.dromara.hutool.core.map.multi.Table;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.poi.excel.ExcelConfig;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Excel写出配置
 *
 * @author Looly
 * @since 6.0.0
 */
public class ExcelWriteConfig extends ExcelConfig {

	/**
	 * 是否只保留别名对应的字段
	 */
	protected boolean onlyAlias;
	/**
	 * 是否强制插入行<br>
	 * 如果为{@code true}，则写入行以下的已存在行下移，{@code false}则利用填充已有行，不存在再创建行
	 */
	protected boolean insertRow = true;
	/**
	 * 标题顺序比较器
	 */
	protected Comparator<String> aliasComparator;

	@Override
	public ExcelWriteConfig setHeaderAlias(final Map<String, String> headerAlias) {
		this.aliasComparator = null;
		return (ExcelWriteConfig) super.setHeaderAlias(headerAlias);
	}

	@Override
	public ExcelWriteConfig addHeaderAlias(final String header, final String alias) {
		this.aliasComparator = null;
		return (ExcelWriteConfig) super.addHeaderAlias(header, alias);
	}

	@Override
	public ExcelWriteConfig removeHeaderAlias(final String header) {
		this.aliasComparator = null;
		return (ExcelWriteConfig) super.removeHeaderAlias(header);
	}

	/**
	 * 设置是否只保留别名中的字段值，如果为true，则不设置alias的字段将不被输出，false表示原样输出
	 * Bean中设置@Alias时，setOnlyAlias是无效的，这个参数只和addHeaderAlias配合使用，原因是注解是Bean内部的操作，而addHeaderAlias是Writer的操作，不互通。
	 *
	 * @param isOnlyAlias 是否只保留别名中的字段值
	 * @return this
	 */
	public ExcelWriteConfig setOnlyAlias(final boolean isOnlyAlias) {
		this.onlyAlias = isOnlyAlias;
		return this;
	}

	/**
	 * 设置是否插入行，如果为true，则写入行以下的已存在行下移，false则利用填充已有行，不存在时创建行
	 *
	 * @param insertRow 是否插入行
	 * @return this
	 */
	public ExcelWriteConfig setInsertRow(final boolean insertRow) {
		this.insertRow = insertRow;
		return this;
	}

	/**
	 * 获取单例的别名比较器，比较器的顺序为别名加入的顺序
	 *
	 * @return Comparator
	 */
	public Comparator<String> getCachedAliasComparator() {
		final Map<String, String> headerAlias = this.headerAlias;
		if (MapUtil.isEmpty(headerAlias)) {
			return null;
		}
		Comparator<String> aliasComparator = this.aliasComparator;
		if (null == aliasComparator) {
			final Set<String> keySet = headerAlias.keySet();
			aliasComparator = new IndexedComparator<>(keySet.toArray(new String[0]));
			this.aliasComparator = aliasComparator;
		}
		return aliasComparator;
	}

	/**
	 * 为指定的key列表添加标题别名，如果没有定义key的别名，在onlyAlias为false时使用原key<br>
	 * key为别名，value为字段值
	 *
	 * @param rowMap 一行数据
	 * @return 别名列表
	 */
	public Table<?, ?, ?> aliasTable(final Map<?, ?> rowMap) {
		final Table<Object, Object, Object> filteredTable = new RowKeyTable<>(new LinkedHashMap<>(), TableMap::new);
		if (MapUtil.isEmpty(headerAlias)) {
			rowMap.forEach((key, value) -> filteredTable.put(key, key, value));
		} else {
			rowMap.forEach((key, value) -> {
				final String aliasName = headerAlias.get(StrUtil.toString(key));
				if (null != aliasName) {
					// 别名键值对加入
					filteredTable.put(key, aliasName, value);
				} else if (!onlyAlias) {
					// 保留无别名设置的键值对
					filteredTable.put(key, key, value);
				}
			});
		}

		return filteredTable;
	}
}
