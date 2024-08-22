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

package org.dromara.hutool.poi.excel.cell.editors;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 单元格编辑器接口<br>
 * 在读取Excel值时，有时我们需要针对所有单元格统一处理结果值（如null转默认值）的情况，实现接口并调用<br>
 * reader.setCellEditor()设置编辑器<br>
 * 此接口可完成以下功能：
 * <ul>
 *     <li>对单元格进行编辑，如修改样式等。</li>
 *     <li>对单元格的值进行编辑，如根据单元格修改不同值，然后返回</li>
 * </ul>
 *
 * @author Looly
 */
@FunctionalInterface
public interface CellEditor {

	/**
	 * 编辑，根据单元格信息处理结果值，返回处理后的结果
	 *
	 * @param cell  单元格对象，可以获取单元格行、列样式等信息
	 * @param value 单元格值
	 * @return 编辑后的值
	 */
	Object edit(Cell cell, Object value);
}
