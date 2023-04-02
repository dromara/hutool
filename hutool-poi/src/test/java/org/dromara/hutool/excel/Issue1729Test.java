package org.dromara.hutool.excel;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * issue#1729@Github<br>
 * 日期为空时返回""而非null，因此会导致日期等字段的转换错误，此处转bean时忽略错误。
 */
public class Issue1729Test {

	@Test
	public void readTest() {
		final ExcelReader reader = ExcelUtil.getReader("UserProjectDO.xlsx");
		final List<UserProjectDO> read = reader.read(0, 1, UserProjectDO.class);
		Assertions.assertEquals("aa", read.get(0).getProjectName());
		Assertions.assertNull(read.get(0).getEndTrainTime());
		Assertions.assertEquals("2020-02-02", read.get(0).getEndTestTime().toString());
	}

	@Data
	public static class UserProjectDO {
		private String projectName;
		private java.sql.Date endTrainTime;
		private java.sql.Date endTestTime;
	}
}
