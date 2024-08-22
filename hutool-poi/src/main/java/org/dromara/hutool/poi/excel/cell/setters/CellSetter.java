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

package org.dromara.hutool.poi.excel.cell.setters;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 单元格值自定义设置器，主要用于Excel数据导出，用户通过自定义此接口，实现可定制化的单元格值设定
 *
 * @author looly
 * @since 5.7.8
 */
@FunctionalInterface
public interface CellSetter {

	/**
	 * 自定义单元格值设置，同时可以设置单元格样式、格式等信息
	 * @param cell 单元格
	 */
	void setValue(Cell cell);
}
