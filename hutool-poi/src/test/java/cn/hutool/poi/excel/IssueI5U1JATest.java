package cn.hutool.poi.excel;

import org.junit.Ignore;
import org.junit.Test;

public class IssueI5U1JATest {
	@Test
	@Ignore
	public void readAllTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/issueI5U1JA.xlsx");
		reader.readAll();
	}
}
