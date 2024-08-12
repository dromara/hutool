/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.poi.excel.reader.sheet;

import org.apache.poi.ss.usermodel.Sheet;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.poi.excel.reader.ExcelReadConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取{@link Sheet}为bean的List列表形式
 *
 * @author looly
 * @since 5.4.4
 * @param <T> 结果类型
 */
public class BeanSheetReader<T> implements SheetReader<List<T>> {

	private final Class<T> beanClass;
	private final MapSheetReader mapSheetReader;

	/**
	 * 构造
	 *
	 * @param headerRowIndex 标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
	 * @param startRowIndex  起始行（包含，从0开始计数）
	 * @param endRowIndex    结束行（包含，从0开始计数）
	 * @param beanClass      每行对应Bean的类型
	 */
	public BeanSheetReader(final int headerRowIndex, final int startRowIndex, final int endRowIndex, final Class<T> beanClass) {
		mapSheetReader = new MapSheetReader(headerRowIndex, startRowIndex, endRowIndex);
		this.beanClass = beanClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> read(final Sheet sheet) {
		final List<Map<String, Object>> mapList = mapSheetReader.read(sheet);
		if (Map.class.isAssignableFrom(this.beanClass)) {
			return (List<T>) mapList;
		}

		final List<T> beanList = new ArrayList<>(mapList.size());
		final CopyOptions copyOptions = CopyOptions.of().setIgnoreError(true);
		for (final Map<String, Object> map : mapList) {
			beanList.add(BeanUtil.toBean(map, this.beanClass, copyOptions));
		}
		return beanList;
	}

	/**
	 * 设置Excel配置
	 *
	 * @param config Excel配置
	 */
	public void setExcelConfig(final ExcelReadConfig config) {
		this.mapSheetReader.setExcelConfig(config);
	}
}
