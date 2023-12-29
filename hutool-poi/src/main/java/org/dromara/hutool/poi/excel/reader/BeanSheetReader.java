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

package org.dromara.hutool.poi.excel.reader;

import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.copier.CopyOptions;
import org.dromara.hutool.poi.excel.cell.CellEditor;
import org.apache.poi.ss.usermodel.Sheet;

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
	 * 设置单元格值处理逻辑<br>
	 * 当Excel中的值并不能满足我们的读取要求时，通过传入一个编辑接口，可以对单元格值自定义，例如对数字和日期类型值转换为字符串等
	 *
	 * @param cellEditor 单元格值处理接口
	 */
	public void setCellEditor(final CellEditor cellEditor) {
		this.mapSheetReader.setCellEditor(cellEditor);
	}

	/**
	 * 设置是否忽略空行
	 *
	 * @param ignoreEmptyRow 是否忽略空行
	 */
	public void setIgnoreEmptyRow(final boolean ignoreEmptyRow) {
		this.mapSheetReader.setIgnoreEmptyRow(ignoreEmptyRow);
	}

	/**
	 * 设置标题行的别名Map
	 *
	 * @param headerAlias 别名Map
	 */
	public void setHeaderAlias(final Map<String, String> headerAlias) {
		this.mapSheetReader.setHeaderAlias(headerAlias);
	}

	/**
	 * 增加标题别名
	 *
	 * @param header 标题
	 * @param alias  别名
	 */
	public void addHeaderAlias(final String header, final String alias) {
		this.mapSheetReader.addHeaderAlias(header, alias);
	}
}
