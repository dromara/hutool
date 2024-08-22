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
