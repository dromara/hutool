/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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
