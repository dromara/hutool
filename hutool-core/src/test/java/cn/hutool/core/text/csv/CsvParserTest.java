package cn.hutool.core.text.csv;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

public class CsvParserTest {
	
	@Test
	public void parseTest1() {
		StringReader reader = StrUtil.getReader("aaa,b\"bba\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		Assert.assertEquals("b\"bba\"", row.getRawList().get(1));
		IoUtil.close(parser);
	}
	
	@Test
	public void parseTest2() {
		StringReader reader = StrUtil.getReader("aaa,\"bba\"bbb,ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		Assert.assertEquals("\"bba\"bbb", row.getRawList().get(1));
		IoUtil.close(parser);
	}
	
	@Test
	public void parseTest3() {
		StringReader reader = StrUtil.getReader("aaa,\"bba\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		Assert.assertEquals("bba", row.getRawList().get(1));
		IoUtil.close(parser);
	}
	
	@Test
	public void parseTest4() {
		StringReader reader = StrUtil.getReader("aaa,\"\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		Assert.assertEquals("", row.getRawList().get(1));
		IoUtil.close(parser);
	}
}
