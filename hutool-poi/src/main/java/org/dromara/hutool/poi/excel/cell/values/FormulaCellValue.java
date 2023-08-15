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

package org.dromara.hutool.poi.excel.cell.values;

import org.dromara.hutool.poi.excel.cell.CellSetter;
import org.dromara.hutool.poi.excel.cell.CellValue;
import org.apache.poi.ss.usermodel.Cell;

/**
 * 公式类型的值<br>
 *
 * <ul>
 *     <li>在Sax读取模式时，此对象用于接收单元格的公式以及公式结果值信息</li>
 *     <li>在写出模式时，用于定义写出的单元格类型为公式</li>
 * </ul>
 *
 * @author looly
 * @since 4.0.11
 */
public class FormulaCellValue implements CellValue<String>, CellSetter {

	/**
	 * 公式
	 */
	private final String formula;
	/**
	 * 结果，使用ExcelWriter时可以不用
	 */
	private final Object result;

	/**
	 * 构造
	 *
	 * @param formula 公式
	 */
	public FormulaCellValue(final String formula) {
		this(formula, null);
	}

	/**
	 * 构造
	 *
	 * @param formula 公式
	 * @param result  结果
	 */
	public FormulaCellValue(final String formula, final Object result) {
		this.formula = formula;
		this.result = result;
	}

	@Override
	public String getValue() {
		return this.formula;
	}

	@Override
	public void setValue(final Cell cell) {
		cell.setCellFormula(this.formula);
	}

	/**
	 * 获取结果
	 * @return 结果
	 */
	public Object getResult() {
		return this.result;
	}

	@Override
	public String toString() {
		return getResult().toString();
	}
}
