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

package org.dromara.hutool.core.io.file;

import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件读取测试
 * @author Looly
 *
 */
public class FileReaderTest {

	@Test
	public void fileReaderTest(){
		final FileReader fileReader = FileReader.of(FileUtil.file("test.properties"));
		final String result = fileReader.readString();
		Assertions.assertNotNull(result);
	}

	@Test
	public void readLinesTest() {
		final FileReader fileReader = FileReader.of(FileUtil.file("test.properties"));
		final List<String> strings = fileReader.readLines();
		Assertions.assertEquals(6, strings.size());
	}

	@Test
	public void readLinesTest2() {
		final FileReader fileReader = FileReader.of(FileUtil.file("test.properties"));
		final List<String> strings = fileReader.readLines(new ArrayList<>(), StrUtil::isNotBlank);
		Assertions.assertEquals(5, strings.size());
	}
}
