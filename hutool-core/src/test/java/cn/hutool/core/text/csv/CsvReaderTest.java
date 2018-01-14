package cn.hutool.core.text.csv;

import java.util.List;

import org.junit.Test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;

public class CsvReaderTest {
	@Test
	public void readTest() {
		CsvReader reader = new CsvReader();
		CsvData data = reader.read(ResourceUtil.getReader("test.csv", CharsetUtil.CHARSET_UTF_8));
		List<CsvRow> rows = data.getRows();
		for (CsvRow csvRow : rows) {
			Console.log(csvRow.get(2));
		}
	}
}
