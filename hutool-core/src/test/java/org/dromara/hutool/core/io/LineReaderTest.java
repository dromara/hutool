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
