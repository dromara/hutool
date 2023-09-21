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

package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.func.SerConsumer;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue2783Test {

	@Test
	@Disabled
	public void readTest() {
		// 测试数据
		final CsvWriter writer = CsvUtil.getWriter("d:/test/big.csv", CharsetUtil.UTF_8);
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			writer.writeLine("aaaa", "bbbb", "ccccc", "dddd");
		}
		writer.close();

		// 读取
		final CsvReader reader = CsvUtil.getReader(FileUtil.getReader("d:/test/big.csv", CharsetUtil.UTF_8));
		reader.read((SerConsumer<CsvRow>) strings -> {

		});
	}
}
