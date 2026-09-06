package cn.hutool.core.text.csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvCommentTest {

	@ParameterizedTest
	@ValueSource(strings = {"\n", "\r", "\r\n"})
	public void commentsPreserveOriginalLineNumbers(String lineEnd) {
		final CsvData data = new CsvReader().readFromStr(
			"# first comment" + lineEnd + "a,b" + lineEnd
				+ "# second comment" + lineEnd + "# third comment" + lineEnd + "c,d");

		assertEquals(2, data.getRowCount());
		assertEquals(Arrays.asList("a", "b"), data.getRow(0).getRawList());
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals(Arrays.asList("c", "d"), data.getRow(1).getRawList());
		assertEquals(4, data.getRow(1).getOriginalLineNumber());
	}

	@ParameterizedTest
	@ValueSource(strings = {"\n", "\r", "\r\n"})
	public void commentsDoNotCreateEmptyRows(String lineEnd) {
		final CsvReadConfig config = CsvReadConfig.defaultConfig().setSkipEmptyRows(false);
		final CsvData data = new CsvReader(config).readFromStr(
			"# comment" + lineEnd + lineEnd + "a,b" + lineEnd + "# final comment" + lineEnd);

		assertEquals(2, data.getRowCount());
		assertEquals("", data.getRow(0).get(0));
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals(Arrays.asList("a", "b"), data.getRow(1).getRawList());
		assertEquals(2, data.getRow(1).getOriginalLineNumber());
	}

	@ParameterizedTest
	@ValueSource(strings = {"\n", "\r", "\r\n"})
	public void headerAfterCommentUsesOriginalLineNumber(String lineEnd) {
		final CsvReadConfig config = CsvReadConfig.defaultConfig().setHeaderLineNo(1);
		final CsvData data = new CsvReader(config).readFromStr(
			"# comment" + lineEnd + "name,value" + lineEnd + "first,1");

		assertEquals(1, data.getRowCount());
		assertEquals("first", data.getRow(0).getByName("name"));
		assertEquals("1", data.getRow(0).getByName("value"));
		assertEquals(2, data.getRow(0).getOriginalLineNumber());
	}

	@ParameterizedTest
	@ValueSource(strings = {"\n", "\r", "\r\n"})
	public void lineRangeAfterCommentUsesOriginalLineNumber(String lineEnd) {
		final CsvReadConfig config = CsvReadConfig.defaultConfig().setBeginLineNo(2).setEndLineNo(2);
		final CsvData data = new CsvReader(config).readFromStr(
			"# comment" + lineEnd + "first,1" + lineEnd + "second,2" + lineEnd + "third,3");

		assertEquals(1, data.getRowCount());
		assertEquals(Arrays.asList("second", "2"), data.getRow(0).getRawList());
		assertEquals(2, data.getRow(0).getOriginalLineNumber());
	}

	@ParameterizedTest
	@ValueSource(strings = {"\n", "\r", "\r\n"})
	public void commentCharactersInQuotedFieldsArePreserved(String lineEnd) {
		final CsvData data = new CsvReader().readFromStr(
			"# comment" + lineEnd + "\"first" + lineEnd + "# still a field\",1" + lineEnd + "last,2");

		assertEquals(2, data.getRowCount());
		assertEquals("first" + lineEnd + "# still a field", data.getRow(0).get(0));
		assertEquals(1, data.getRow(0).getOriginalLineNumber());
		assertEquals(3, data.getRow(1).getOriginalLineNumber());
	}

	@Test
	public void customCommentCharacterWithMixedLineEndings() {
		final CsvReadConfig config = CsvReadConfig.defaultConfig().setCommentCharacter(';');
		final CsvData data = new CsvReader(config).readFromStr("; first\r\n; second\n; third\ra,b\r\n");

		assertEquals(1, data.getRowCount());
		assertEquals(Arrays.asList("a", "b"), data.getRow(0).getRawList());
		assertEquals(3, data.getRow(0).getOriginalLineNumber());
	}

	@Test
	public void disabledCommentsRemainData() {
		final CsvReadConfig config = CsvReadConfig.defaultConfig().disableComment();
		final CsvData data = new CsvReader(config).readFromStr("# first\r\n# second\r\n");

		assertEquals(2, data.getRowCount());
		assertEquals("# first", data.getRow(0).get(0));
		assertEquals(0, data.getRow(0).getOriginalLineNumber());
		assertEquals("# second", data.getRow(1).get(0));
		assertEquals(1, data.getRow(1).getOriginalLineNumber());
	}
}
