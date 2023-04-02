package org.dromara.hutool.csv;

import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.text.StrUtil;
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
