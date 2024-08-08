package org.dromara.hutool.poi.excel.writer;

import lombok.Getter;
import org.dromara.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ExcelWriteBeanTest {
	@Test
	@Disabled
	public void writeRowTest() {
		final MyBean bean = new MyBean("value1", "value2");

		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeRowTest.xlsx");
		writer.writeRow(bean, true);
		writer.close();
	}

	@Getter
	static class MyBean {
		private final String property1;
		private final String property2;

		public MyBean(final String property1, final String property2) {
			this.property1 = property1;
			this.property2 = property2;
		}
	}
}
