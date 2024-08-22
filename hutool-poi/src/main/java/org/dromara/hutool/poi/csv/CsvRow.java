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

package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.core.collection.ListWrapper;
import org.dromara.hutool.core.lang.Assert;

import java.util.*;

/**
 * CSV中一行的表示
 *
 * @author Looly
 */
public final class CsvRow extends ListWrapper<String> {

	/** 原始行号 */
	private final long originalLineNumber;

	final Map<String, Integer> headerMap;

	/**
	 * 构造
	 *
	 * @param originalLineNumber 对应文件中的第几行
	 * @param headerMap 标题Map
	 * @param fields 数据列表
	 */
	public CsvRow(final long originalLineNumber, final Map<String, Integer> headerMap, final List<String> fields) {
		super(Assert.notNull(fields, "fields must be not null!"));
		this.originalLineNumber = originalLineNumber;
		this.headerMap = headerMap;
	}

	/**
	 * 获取原始行号，多行情况下为首行行号。忽略注释行
	 *
	 * @return the original line number 行号
	 */
	public long getOriginalLineNumber() {
		return originalLineNumber;
	}

	/**
	 * 获取标题对应的字段内容
	 *
	 * @param name 标题名
	 * @return 字段值，null表示无此字段值
	 * @throws IllegalStateException CSV文件无标题行抛出此异常
	 */
	public String getByName(final String name) {
		Assert.notNull(this.headerMap, "No header available!");

		final Integer col = headerMap.get(name);
		if (col != null) {
			return get(col);
		}
		return null;
	}

	/**
	 * 获取标题与字段值对应的Map
	 *
	 * @return 标题与字段值对应的Map
	 * @throws IllegalStateException CSV文件无标题行抛出此异常
	 */
	public Map<String, String> getFieldMap() {
		if (headerMap == null) {
			throw new IllegalStateException("No header available");
		}

		final Map<String, String> fieldMap = new LinkedHashMap<>(headerMap.size(), 1);
		String key;
		Integer col;
		String val;
		for (final Map.Entry<String, Integer> header : headerMap.entrySet()) {
			key = header.getKey();
			col = headerMap.get(key);
			val = null == col ? null : get(col);
			fieldMap.put(key, val);
		}

		return fieldMap;
	}

	/**
	 * 一行数据转换为Bean对象，忽略转换错误
	 *
	 * @param <T> Bean类型
	 * @param clazz bean类
	 * @return Bean
	 * @since 5.3.6
	 */
	public <T> T toBean(final Class<T> clazz){
		return BeanUtil.toBean(getFieldMap(), clazz, CopyOptions.of().setIgnoreError(true));
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return new HashSet<>(this.raw).containsAll(c);
	}

	@Override
	public String get(final int index) {
		return index >= size() ? null : super.get(index);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CsvRow{");
		sb.append("originalLineNumber=");
		sb.append(originalLineNumber);
		sb.append(", ");

		sb.append("fields=");
		if (headerMap != null) {
			sb.append('{');
			for (final Iterator<Map.Entry<String, String>> it = getFieldMap().entrySet().iterator(); it.hasNext();) {

				final Map.Entry<String, String> entry = it.next();
				sb.append(entry.getKey());
				sb.append('=');
				if (entry.getValue() != null) {
					sb.append(entry.getValue());
				}
				if (it.hasNext()) {
					sb.append(", ");
				}
			}
			sb.append('}');
		} else {
			sb.append(this.raw.toString());
		}

		sb.append('}');
		return sb.toString();
	}
}
