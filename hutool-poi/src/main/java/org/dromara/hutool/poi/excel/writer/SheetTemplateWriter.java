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

import org.apache.poi.ss.usermodel.Sheet;
import org.dromara.hutool.core.map.BeanMap;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Map;

/**
 * 模板Excel写入器<br>
 * 解析已有模板，并填充模板中的变量为数据
 *
 * @author Looly
 * @since 6.0.0
 */
public class SheetTemplateWriter {

	private final Sheet sheet;
	private final ExcelWriteConfig config;
	/**
	 * 模板上下文，存储模板中变量及其位置信息
	 */
	private final TemplateContext templateContext;

	/**
	 * 构造
	 *
	 * @param sheet  {@link Sheet}
	 * @param config Excel写配置
	 */
	public SheetTemplateWriter(final Sheet sheet, final ExcelWriteConfig config) {
		this.sheet = sheet;
		this.config = config;
		this.templateContext = new TemplateContext(sheet);
	}

	/**
	 * 填充非列表模板变量（一次性变量）
	 *
	 * @param rowMap 行数据
	 * @return this
	 */
	public SheetTemplateWriter fillOnce(final Map<?, ?> rowMap) {
		rowMap.forEach((key, value) -> this.templateContext.fill(StrUtil.toStringOrNull(key), rowMap, false));
		return this;
	}

	/**
	 * 填充模板行，用于列表填充
	 *
	 * @param rowBean 行的Bean数据
	 * @return this
	 */
	public SheetTemplateWriter fillRow(final Object rowBean) {
		// TODO 支持Bean的级联属性获取
		return fillRow(new BeanMap(rowBean));
	}

	/**
	 * 填充模板行，用于列表填充
	 *
	 * @param rowMap 行数据
	 * @return this
	 */
	public SheetTemplateWriter fillRow(final Map<?, ?> rowMap) {
		if (this.config.insertRow) {
			// 当前填充行的模板行以下全部下移
			final int bottomRowIndex = this.templateContext.getBottomRowIndex(rowMap);
			if (bottomRowIndex < 0) {
				// 无可填充行
				return this;
			}
			if (bottomRowIndex != 0) {
				final int lastRowNum = this.sheet.getLastRowNum();
				if (bottomRowIndex <= lastRowNum) {
					// 填充行底部需有数据，无数据跳过
					// 虚拟行的行号就是需要填充的行，这行的已有数据整体下移
					this.sheet.shiftRows(bottomRowIndex, this.sheet.getLastRowNum(), 1);
				}
			}
		}

		rowMap.forEach((key, value) -> this.templateContext.fill(StrUtil.toStringOrNull(key), rowMap, true));

		return this;
	}
}
