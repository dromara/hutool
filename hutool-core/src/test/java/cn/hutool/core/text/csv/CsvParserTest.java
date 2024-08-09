package cn.hutool.core.text.csv;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class CsvParserTest {

	@Test
	public void parseTest1() {
		StringReader reader = StrUtil.getReader("aaa,b\"bba\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		assertEquals("b\"bba\"", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	public void parseTest2() {
		StringReader reader = StrUtil.getReader("aaa,\"bba\"bbb,ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		assertEquals("\"bba\"bbb", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	public void parseTest3() {
		StringReader reader = StrUtil.getReader("aaa,\"bba\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		assertEquals("bba", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	public void parseTest4() {
		StringReader reader = StrUtil.getReader("aaa,\"\",ccc");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		//noinspection ConstantConditions
		assertEquals("", row.getRawList().get(1));
		IoUtil.close(parser);
	}

	@Test
	public void parseEscapeTest(){
		// https://datatracker.ietf.org/doc/html/rfc4180#section-2
		// 第七条规则
		StringReader reader = StrUtil.getReader("\"b\"\"bb\"");
		CsvParser parser = new CsvParser(reader, null);
		CsvRow row = parser.nextRow();
		assertNotNull(row);
		assertEquals(1, row.size());
		assertEquals("b\"bb", row.get(0));
	}
}
