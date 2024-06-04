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

package org.dromara.hutool.poi.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellReference;

/**
 * 样式集合接口<br>
 * 通过自定义样式集合，可根据单元格不同、值的不同设置不同的样式
 *
 * @author Looly
 * @since 6.0.0
 */
public interface StyleSet {

	/**
	 * 获取单元格样式，可以：
	 * <ul>
	 *     <li>根据单元格位置获取定义不同的样式，如首行、首列、偶数行、偶数列等</li>
	 *     <li>根据单元格值获取定义不同的样式，如数字、日期等，也可根据是否为标题行定义独立的样式</li>
	 * </ul>
	 *
	 * @param reference 单元格引用，包含单元格位置等信息
	 * @param cellValue 单元格值
	 * @param isHeader  是否为表头，扁头定义的特殊样式
	 * @return 单元格样式
	 */
	CellStyle getStyleFor(CellReference reference, Object cellValue, boolean isHeader);
}
