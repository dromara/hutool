package cn.hutool.core.text.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * https://gitee.com/dromara/hutool/issues/IA8WE0
 */
public class IssueIA8WE0Test {
	@Test
	public void csvReadTest() {
		final CsvReader csvReader = new CsvReader();
		final CsvData read = csvReader.read(FileUtil.file("issueIA8WE0.csv"));
		final List<CsvRow> rows = read.getRows();

		assertEquals(1, rows.size());
		assertEquals(3, rows.get(0).size());
		assertEquals("c1_text1", rows.get(0).get(0));
		// 如果\n#出现在双引号中，表示实际的文本内容，并不算注释
		assertEquals("c1_text2\n#c1_text2_line2", rows.get(0).get(1));
		assertEquals("c1_text3", rows.get(0).get(2));

		IoUtil.close(csvReader);
	}
}
