package cn.hutool.poi.csv;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CsvParserTest {

	@Test
	public void parseTest1() {
		final StringReader reader = StrUtil.getReader("aaa,b\"bba\",ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assert.assertEquals("b\"bba\"", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	public void parseTest2() {
		final StringReader reader = StrUtil.getReader("aaa,\"bba\"bbb,ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assert.assertEquals("\"bba\"bbb", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	public void parseTest3() {
		final StringReader reader = StrUtil.getReader("aaa,\"bba\",ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assert.assertEquals("bba", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	public void parseTest4() {
		final StringReader reader = StrUtil.getReader("aaa,\"\",ccc");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		Assert.assertEquals("", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	public void parseEscapeTest(){
		// https://datatracker.ietf.org/doc/html/rfc4180#section-2
		// 第七条规则
		final StringReader reader = StrUtil.getReader("\"b\"\"bb\"");
		final CsvParser parser = new CsvParser(reader, null);
		final CsvRow row = parser.nextRow();
		Assert.assertNotNull(row);
		Assert.assertEquals(1, row.size());
		Assert.assertEquals("b\"bb", row.get(0));
	}
}
