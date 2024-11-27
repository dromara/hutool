package cn.hutool.core.text.csv;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class IssueIB5UQ8Test {
	@Test
	void parseEscapeTest() {
		String csv = "\"Consultancy, 10\"\",, food\"";
		final CsvReader reader = CsvUtil.getReader(new StringReader(csv));
		final String s = reader.read().getRow(0).get(0);
		Console.log(s);
	}
}
