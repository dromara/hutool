package cn.hutool.poi.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.func.SerConsumer;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Ignore;
import org.junit.Test;

public class Issue2783Test {

	@Test
	@Ignore
	public void readTest() {
//		final CsvWriter writer = CsvUtil.getWriter("d:/test/big.csv", CharsetUtil.UTF_8);
//		for (int i = 0; i < Integer.MAX_VALUE; i++) {
//			writer.writeLine("aaaa", "bbbb", "ccccc", "dddd");
//		}
//		writer.close();

		final CsvReader reader = CsvUtil.getReader(FileUtil.getReader("d:/test/big.csv", CharsetUtil.UTF_8));
		reader.read((SerConsumer<CsvRow>) strings -> {

		});
	}
}
