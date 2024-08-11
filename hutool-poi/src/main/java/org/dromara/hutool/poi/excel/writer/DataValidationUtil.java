/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
