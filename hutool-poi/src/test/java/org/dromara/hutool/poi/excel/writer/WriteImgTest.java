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

package org.dromara.hutool.poi.excel.writer;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class WriteImgTest {
	@Test
	@Disabled
	void writeTest() {
		final File file = FileUtil.file("d:/test/hutool.png");
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/images.xlsx");
		for (int i = 0; i < 6; i++) {
			writer.writeRow(ListUtil.of(file, file, file, file, file, file));
		}
		writer.close();
	}
}