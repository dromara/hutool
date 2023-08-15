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
