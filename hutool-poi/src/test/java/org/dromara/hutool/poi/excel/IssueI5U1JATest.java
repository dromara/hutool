package org.dromara.hutool.poi.excel;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI5U1JATest {
	@Test
	@Disabled
	public void readAllTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/issueI5U1JA.xlsx");
		reader.readAll();
	}
}
