/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel.writer;

import org.dromara.hutool.core.comparator.IndexedComparator;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.poi.excel.ExcelConfig;

import java.util.Comparator;
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
}
