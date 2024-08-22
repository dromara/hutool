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

package org.dromara.hutool.poi.excel.reader.sheet;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * Excel {@link Sheet}读取接口，通过实现此接口，将{@link Sheet}中的数据读取为不同类型。
 *
 * @param <T> 读取的数据类型
 */
@FunctionalInterface
public interface SheetReader<T> {

	/**
	 * 读取数据
	 *
	 * @param sheet {@link Sheet}
	 * @return 读取结果
	 */
	T read(Sheet sheet);
}
