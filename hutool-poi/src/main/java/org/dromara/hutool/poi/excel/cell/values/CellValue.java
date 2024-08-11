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

package org.dromara.hutool.poi.excel.cell.values;

/**
 * 抽象的单元格值接口，用于判断不同类型的单元格值<br>
 * 通过自定义的此接口，对于复杂的单元格值类型，可以自定义读取值的类型，如数字、公式等。
 *
 * @param <T> 值得类型
 * @author looly
 * @since 4.0.11
 */
public interface CellValue<T> {

	/**
	 * 获取单元格值
	 *
	 * @return 值
	 */
	T getValue();
}
