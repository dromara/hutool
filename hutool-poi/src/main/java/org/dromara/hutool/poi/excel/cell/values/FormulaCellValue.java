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

package org.dromara.hutool.poi.excel.cell.values;

import org.apache.poi.ss.usermodel.CellType;
import org.dromara.hutool.poi.excel.cell.setters.CellSetter;
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
	private final CellType resultType;

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
		this(formula, result, null);
	}

	/**
	 * 构造
	 *
	 * @param formula    公式
	 * @param result     结果
	 * @param resultType 结果类型
	 */
	public FormulaCellValue(final String formula, final Object result, final CellType resultType) {
		this.formula = formula;
		this.result = result;
		this.resultType = resultType;
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
	 *
	 * @return 结果
	 */
	public Object getResult() {
		return this.result;
	}

	/**
	 * 获取结果类型
	 *
	 * @return 结果类型，{@code null}表示未明确
	 */
	public CellType getResultType() {
		return this.resultType;
	}

	@Override
	public String toString() {
		return getResult().toString();
	}
}
