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

package org.dromara.hutool.poi.excel.writer;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

/**
 * Excel数据验证相关工具
 *
 * @author Looly
 * @since 6.0.0
 */
public class DataValidationUtil {

	/**
	 * 增加下拉列表
	 *
	 * @param sheet      {@link Sheet}
	 * @param regions    {@link CellRangeAddressList} 指定下拉列表所占的单元格范围
	 * @param selectList 下拉列表内容
	 * @since 4.6.2
	 */
	public static void addSelect(final Sheet sheet, final CellRangeAddressList regions, final String... selectList) {
		final DataValidationHelper validationHelper = sheet.getDataValidationHelper();
		final DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(selectList);

		//设置下拉框数据
		final DataValidation dataValidation = validationHelper.createValidation(constraint, regions);

		//处理Excel兼容性问题
		if (dataValidation instanceof XSSFDataValidation) {
			dataValidation.setSuppressDropDownArrow(true);
			dataValidation.setShowErrorBox(true);
		} else {
			dataValidation.setSuppressDropDownArrow(false);
		}

		sheet.addValidationData(dataValidation);
	}
}
