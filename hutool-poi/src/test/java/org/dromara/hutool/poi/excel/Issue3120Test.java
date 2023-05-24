/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 *
 */
public class Issue3120Test {

	@Test
	void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("issue3120.xlsx");
		final List<List<Object>> read = reader.read(2, Integer.MAX_VALUE, false);
		Assertions.assertEquals("[1, null, 100, null, 20]", read.get(0).toString());
		Assertions.assertEquals("[32, null, 200, null, 30]", read.get(1).toString());
	}

}
