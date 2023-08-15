/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel.reader;

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
