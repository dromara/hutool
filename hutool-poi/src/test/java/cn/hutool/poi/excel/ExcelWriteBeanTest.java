package cn.hutool.poi.excel;

import lombok.Getter;
import org.junit.Ignore;
import org.junit.Test;

public class ExcelWriteBeanTest {

	@Test
	@Ignore
	public void writeRowTest() {
		MyBean bean = new MyBean("value1", "value2");

		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeRowTest.xlsx");
		writer.writeRow(bean, true);
		writer.close();
	}

	@Getter
	static class MyBean {
		private final String property1;
		private final String property2;

		public MyBean(String property1, String property2) {
			this.property1 = property1;
			this.property2 = property2;
		}
	}
}
