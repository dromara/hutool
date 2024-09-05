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

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 按照 https://datatracker.ietf.org/doc/html/rfc4180#section-2<br>
 * 如果字段正文中出现双引号，需要使用两个双引号表示转义，并整段使用引号包裹
 */
public class Pr1244Test {

	/**
	 * 此测试中没有引号包裹，则所有引号都被当作内容
	 */
	@Test
	void csvReadTest() {
		final String csv = "a,q\"\"e,d,f";
		final CsvReader reader = CsvUtil.getReader(new StringReader(csv));
		final CsvData read = reader.read();
		assertEquals(4, read.getRow(0).size());
		assertEquals("a", read.getRow(0).get(0));
		assertEquals("q\"\"e", read.getRow(0).get(1));
		assertEquals("d", read.getRow(0).get(2));
		assertEquals("f", read.getRow(0).get(3));
	}

	/**
	 * 此测试中没有引号包裹，则所有引号都被当作内容
	 */
	@Test
	void csvReadTest2() {
		final String csv = "a,q\"e,d,f";
		final CsvReader reader = CsvUtil.getReader(new StringReader(csv));
		final CsvData read = reader.read();
		assertEquals(4, read.getRow(0).size());
		assertEquals("a", read.getRow(0).get(0));
		assertEquals("q\"e", read.getRow(0).get(1));
		assertEquals("d", read.getRow(0).get(2));
		assertEquals("f", read.getRow(0).get(3));
	}
}
