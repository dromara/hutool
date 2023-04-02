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

package org.dromara.hutool.excel.cell.setters;

import org.dromara.hutool.excel.cell.CellSetter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;

/**
 * {@link Hyperlink} 值单元格设置器
 *
 * @author looly
 * @since 5.7.13
 */
public class HyperlinkCellSetter implements CellSetter {

	private final Hyperlink value;

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	HyperlinkCellSetter(final Hyperlink value) {
		this.value = value;
	}

	@Override
	public void setValue(final Cell cell) {
		cell.setHyperlink(this.value);
		cell.setCellValue(this.value.getLabel());
	}
}
