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

package org.dromara.hutool.poi.excel;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.poi.excel.cell.editors.CellEditor;
import org.dromara.hutool.poi.excel.cell.CellReferenceUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel读取和写出通用配置
 *
 * @author Looly
 * @since 6.0.0
 */
public class ExcelConfig {
	/**
	 * 标题行别名
	 */
	protected Map<String, String> headerAlias;
	/**
	 * 单元格值处理接口
	 */
	protected CellEditor cellEditor;

	/**
	 * 获得标题行的别名Map
	 *
	 * @return 别名Map
	 */
	public Map<String, String> getHeaderAlias() {
		return headerAlias;
	}

	/**
	 * 设置标题行的别名Map
	 *
	 * @param headerAlias 别名Map
	 * @return this
	 */
	public ExcelConfig setHeaderAlias(final Map<String, String> headerAlias) {
		this.headerAlias = headerAlias;
		return this;
	}

	/**
	 * 增加标题别名
	 *
	 * @param header 标题
	 * @param alias  别名
	 * @return this
	 */
	public ExcelConfig addHeaderAlias(final String header, final String alias) {
		Map<String, String> headerAlias = this.headerAlias;
		if (null == headerAlias) {
			headerAlias = new LinkedHashMap<>();
			this.headerAlias = headerAlias;
		}
		headerAlias.put(header, alias);
		return this;
	}

	/**
	 * 去除标题别名
	 *
	 * @param header 标题
	 * @return this
	 */
	public ExcelConfig removeHeaderAlias(final String header) {
		this.headerAlias.remove(header);
		return this;
	}

	/**
	 * 清空标题别名，key为Map中的key，value为别名
	 *
	 * @return this
	 */
	public ExcelConfig clearHeaderAlias() {
		return setHeaderAlias(null);
	}

	/**
	 * 转换标题别名，如果没有别名则使用原标题，当标题为空时，列号对应的字母便是header
	 *
	 * @param headerList 原标题列表
	 * @return 转换别名列表
	 */
	public List<Object> aliasHeader(final List<Object> headerList) {
		if (CollUtil.isEmpty(headerList)) {
			return new ArrayList<>(0);
		}

		final int size = headerList.size();
		final List<Object> result = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			result.add(aliasHeader(headerList.get(i), i));
		}
		return result;
	}

	/**
	 * 转换标题别名，如果没有别名则使用原标题，当标题为空时，列号对应的字母便是header
	 *
	 * @param headerObj 原标题
	 * @param index     标题所在列号，当标题为空时，列号对应的字母便是header
	 * @return 转换别名列表
	 */
	public Object aliasHeader(final Object headerObj, final int index) {
		if (null == headerObj) {
			return CellReferenceUtil.indexToColName(index);
		}

		if (null != this.headerAlias) {
			return ObjUtil.defaultIfNull(this.headerAlias.get(headerObj.toString()), headerObj);
		}
		return headerObj;
	}

	/**
	 * 获取单元格值处理器
	 *
	 * @return 单元格值处理器
	 */
	public CellEditor getCellEditor() {
		return this.cellEditor;
	}

	/**
	 * 设置单元格值处理逻辑<br>
	 * 当Excel中的值并不能满足我们的读取要求时，通过传入一个编辑接口，可以对单元格值自定义，例如对数字和日期类型值转换为字符串等
	 *
	 * @param cellEditor 单元格值处理接口
	 * @return this
	 */
	public ExcelConfig setCellEditor(final CellEditor cellEditor) {
		this.cellEditor = cellEditor;
		return this;
	}
}
