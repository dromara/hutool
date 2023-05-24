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

package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * https://github.com/dromara/hutool/issues/3088
 */
public class Issue3088Test {
	@Test
	@Disabled
	void readTest() {
		final CsvReader reader = CsvUtil.getReader();
		final CsvData read = reader.read(FileUtil.file("d:/test/csv_import_template.1.csv"));

		Console.log(read.getRowCount());
	}
}
