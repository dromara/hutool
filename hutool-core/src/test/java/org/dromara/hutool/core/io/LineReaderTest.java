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

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class LineReaderTest {
	@Test
	public void readLfTest() {
		final LineReader lineReader = new LineReader(ResourceUtil.getUtf8Reader("multi_line.properties"));
		final ArrayList<String> list = ListUtil.of(lineReader);
		Assertions.assertEquals(3, list.size());
		Assertions.assertEquals("test1", list.get(0));
		Assertions.assertEquals("test2=abcd\\e", list.get(1));
		Assertions.assertEquals("test3=abc", list.get(2));
	}

	@Test
	public void readCrLfTest() {
		final LineReader lineReader = new LineReader(ResourceUtil.getUtf8Reader("multi_line_crlf.properties"));
		final ArrayList<String> list = ListUtil.of(lineReader);
		Assertions.assertEquals(3, list.size());
		Assertions.assertEquals("test1", list.get(0));
		Assertions.assertEquals("test2=abcd\\e", list.get(1));
		Assertions.assertEquals("test3=abc", list.get(2));
	}
}
