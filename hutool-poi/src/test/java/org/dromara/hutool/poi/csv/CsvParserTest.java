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
		Assertions.assertEquals("b\"bba\"", row.getRaw().get(1));
		IoUtil.closeQuietly(parser);
	}

	@Test
	public void parseTest2() {
		final StringReader reader = StrUtil.getReader("aaa,\"bba\"bbb,ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assertions.assertEquals("\"bba\"bbb", row.getRaw().get(1));
		IoUtil.closeQuietly(parser);
	}

	@Test
	public void parseTest3() {
		final StringReader reader = StrUtil.getReader("aaa,\"bba\",ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assertions.assertEquals("bba", row.getRaw().get(1));
		IoUtil.closeQuietly(parser);
	}

	@Test
	public void parseTest4() {
		final StringReader reader = StrUtil.getReader("aaa,\"\",ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assertions.assertEquals("", row.getRaw().get(1));
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

	@Test
	void issueIB5UQ8Test() {
		final String csv = "\"Consultancy, 10\"\",, food\"";
		final CsvReader reader = CsvUtil.getReader(new StringReader(csv));
		final String s = reader.read().getRow(0).get(0);
		Assertions.assertEquals("Consultancy, 10\",, food", s);
	}

	@Test
	void textDelimiterAtEndTest() {
		final String csv = "\"Consultancy, 10\"";
		final CsvReader reader = CsvUtil.getReader(new StringReader(csv));
		final String s = reader.read().getRow(0).get(0);
		Assertions.assertEquals("Consultancy, 10", s);
	}

	@Test
	void textDelimiterUncloseTest() {
		// 未闭合的文本包装符，文本结尾自动结束，文本包装符
		final String csv = "\"Consultancy,";
		final CsvReader reader = CsvUtil.getReader(new StringReader(csv));
		final String s = reader.read().getRow(0).get(0);
		Assertions.assertEquals("Consultancy,", s);
	}

	@Test
	void textDelimiterOfCount3Test() {
		// 未闭合的文本包装符，文本结尾自动结束，文本包装符
		final String csv = "\"\"\"";
		final CsvParser csvParser = new CsvParser(new StringReader(csv), CsvReadConfig.of().setSkipEmptyRows(false));
		final CsvRow row = csvParser.nextRow();
		Assertions.assertNotNull(row);
		Assertions.assertEquals("\"", row.get(0));
	}
}
