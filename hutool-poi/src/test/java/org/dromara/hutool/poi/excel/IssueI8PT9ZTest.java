package org.dromara.hutool.poi.excel;

import lombok.Data;
import org.dromara.hutool.core.annotation.Alias;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class IssueI8PT9ZTest {

	@Test
	@Disabled
	void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("d:/test/test.xlsx");
		final List<Map<String, Object>> read = reader.read(2, 4, Integer.MAX_VALUE);
		for (final Map<String, Object> stringObjectMap : read) {
			Console.log(stringObjectMap);
		}

		final List<PunchCard> read1 = reader.read(2, 4, PunchCard.class);
		for (final PunchCard punchCard : read1) {
			Console.log(punchCard);
		}
	}

	@Data
	static class PunchCard{
		@Alias("日期")
		private String date;
		@Alias("姓名")
		private String name;
	}
}
