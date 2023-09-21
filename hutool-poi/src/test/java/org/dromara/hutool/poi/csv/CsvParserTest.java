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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class CsvParserTest {

	@Test
	public void parseTest1() {
		final StringReader reader = StrUtil.getReader("aaa,b\"bba\",ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assertions.assertEquals("b\"bba\"", row.getRawList().get(1));
		IoUtil.closeQuietly(parser);
	}

	@Test
	public void parseTest2() {
		final StringReader reader = StrUtil.getReader("aaa,\"bba\"bbb,ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assertions.assertEquals("\"bba\"bbb", row.getRawList().get(1));
		IoUtil.closeQuietly(parser);
	}

	@Test
	public void parseTest3() {
		final StringReader reader = StrUtil.getReader("aaa,\"bba\",ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assertions.assertEquals("bba", row.getRawList().get(1));
		IoUtil.closeQuietly(parser);
	}

	@Test
	public void parseTest4() {
		final StringReader reader = StrUtil.getReader("aaa,\"\",ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assertions.assertEquals("", row.getRawList().get(1));
		IoUtil.closeQuietly(parser);
	}

	@Test
	public void parseEscapeTest(){
		// https://datatracker.ietf.org/doc/html/rfc4180#section-2
		// 第七条规则
		final StringReader reader = StrUtil.getReader("\"b\"\"bb\"");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		Assertions.assertNotNull(row);
		Assertions.assertEquals(1, row.size());
		Assertions.assertEquals("b\"bb", row.get(0));
	}
}
