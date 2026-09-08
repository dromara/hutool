/*
 * Copyright (c) 2026 Hutool Team.
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

package cn.hutool.v7.poi.csv;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvCommentTest {

	@Test
	public void commentsPreserveOriginalLineNumbers() {
		final String lineEnd = "\r\n";

		final CsvData data = new CsvReader().readFromStr(
			"# first comment" + lineEnd + "a,b" + lineEnd
				+ "# second comment" + lineEnd + "# third comment" + lineEnd + "c,d");

		assertEquals(2, data.getRowCount());
		assertEquals(Arrays.asList("a", "b"), data.getRow(0));
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals(Arrays.asList("c", "d"), data.getRow(1));
		assertEquals(4, data.getRow(1).getOriginalLineNumber());
	}

	@Test
	public void commentsDoNotCreateEmptyRows() {
		final String lineEnd = "\r\n";

		final CsvReadConfig config = CsvReadConfig.of().setSkipEmptyRows(false);
		final CsvData data = new CsvReader(config).readFromStr(
			"# comment" + lineEnd + lineEnd + "a,b" + lineEnd + "# final comment" + lineEnd);

		assertEquals(2, data.getRowCount());
		assertEquals("", data.getRow(0).get(0));
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals(Arrays.asList("a", "b"), data.getRow(1));
		assertEquals(2, data.getRow(1).getOriginalLineNumber());
	}

	@Test
	public void headerAfterCommentUsesOriginalLineNumber() {
		final String lineEnd = "\r\n";

		final CsvReadConfig config = CsvReadConfig.of().setHeaderLineNo(1);
		final CsvData data = new CsvReader(config).readFromStr(
			"# comment" + lineEnd + "name,value" + lineEnd + "first,1");

		assertEquals(1, data.getRowCount());
		assertEquals("first", data.getRow(0).getByName("name"));
		assertEquals("1", data.getRow(0).getByName("value"));
		assertEquals(2, data.getRow(0).getOriginalLineNumber());
	}

	@Test
	public void lineRangeAfterCommentUsesOriginalLineNumber() {
		final String lineEnd = "\r\n";

		final CsvReadConfig config = CsvReadConfig.of().setBeginLineNo(2).setEndLineNo(2);
		final CsvData data = new CsvReader(config).readFromStr(
			"# comment" + lineEnd + "first,1" + lineEnd + "second,2" + lineEnd + "third,3");

		assertEquals(1, data.getRowCount());
		assertEquals(Arrays.asList("second", "2"), data.getRow(0));
		assertEquals(2, data.getRow(0).getOriginalLineNumber());
	}

	@Test
	public void commentCharactersInQuotedFieldsArePreserved() {
		final String lineEnd = "\r\n";

		final CsvData data = new CsvReader().readFromStr(
			"# comment" + lineEnd + "\"first" + lineEnd + "# still a field\",1" + lineEnd + "last,2");

		assertEquals(2, data.getRowCount());
		assertEquals("first" + lineEnd + "# still a field", data.getRow(0).get(0));
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals(3, data.getRow(1).getOriginalLineNumber());
	}

	@Test
	public void customCommentCharacterWithMixedLineEndings() {
		final CsvReadConfig config = CsvReadConfig.of().setCommentCharacter(';');
		final CsvData data = new CsvReader(config).readFromStr("; first\r\n; second\n; third\ra,b\r\n");

		assertEquals(1, data.getRowCount());
		assertEquals(Arrays.asList("a", "b"), data.getRow(0));
		assertEquals(3, data.getRow(0).getOriginalLineNumber());
	}

	@Test
	public void disabledCommentsRemainData() {
		final CsvReadConfig config = CsvReadConfig.of().disableComment();
		final CsvData data = new CsvReader(config).readFromStr("# first\r\n# second\r\n");

		assertEquals(2, data.getRowCount());
		assertEquals("# first", data.getRow(0).get(0));
		assertEquals(0, data.getRow(0).getOriginalLineNumber());
		assertEquals("# second", data.getRow(1).get(0));
		assertEquals(1, data.getRow(1).getOriginalLineNumber());
	}
}
