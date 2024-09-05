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

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.io.file.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BomReaderTest {
	@Test
	public void readTest() {
		final BomReader bomReader = FileUtil.getBOMReader(FileUtil.file("with_bom.txt"));
		final String read = IoUtil.read(bomReader, true);
		Assertions.assertEquals("此文本包含BOM头信息，用于测试BOM头读取", read);
	}
}
