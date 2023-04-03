package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.func.SerConsumer;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue2783Test {

	@Test
	@Disabled
	public void readTest() {
		// 测试数据
		final CsvWriter writer = CsvUtil.getWriter("d:/test/big.csv", CharsetUtil.UTF_8);
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			writer.writeLine("aaaa", "bbbb", "ccccc", "dddd");
		}
		writer.close();

		// 读取
		final CsvReader reader = CsvUtil.getReader(FileUtil.getReader("d:/test/big.csv", CharsetUtil.UTF_8));
		reader.read((SerConsumer<CsvRow>) strings -> {

		});
	}
}
