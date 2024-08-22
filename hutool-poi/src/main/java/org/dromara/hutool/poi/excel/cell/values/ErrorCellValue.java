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

import org.dromara.hutool.core.text.StrUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaError;

/**
 * ERROR类型单元格值
 *
 * @author looly
 * @since 5.7.8
 */
public class ErrorCellValue implements CellValue<String> {

	private final Cell cell;

	/**
	 * 构造
	 *
	 * @param cell {@link Cell}
	 */
	public ErrorCellValue(final Cell cell){
		this.cell = cell;
	}

	@Override
	public String getValue() {
		final FormulaError error = FormulaError.forInt(cell.getErrorCellValue());
		return (null == error) ? StrUtil.EMPTY : error.getString();
	}
}
