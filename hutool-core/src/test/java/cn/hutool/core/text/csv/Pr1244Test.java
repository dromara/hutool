package cn.hutool.core.text.csv;

import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 按照 https://datatracker.ietf.org/doc/html/rfc4180#section-2<br>
 * 如果字段正文中出现双引号，需要使用两个双引号表示转义
 */
public class Pr1244Test {
	@Test
	public void csvReadTest() {
		final String csv = "a,q\"e,d,f";
		final CsvReader reader = CsvUtil.getReader(new StringReader(csv));
		final CsvData read = reader.read();
		assertEquals(4, read.getRow(0).size());
		assertEquals("a", read.getRow(0).get(0));
		assertEquals("q\"e", read.getRow(0).get(1));
		assertEquals("d", read.getRow(0).get(2));
		assertEquals("f", read.getRow(0).get(3));
	}
}
