package cn.hutool.poi.excel;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.poi.excel.cell.FormulaCellValue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WriteNumberToStringTest {
	@Test
	@Disabled
	public void writeNumberTest() {
		final ExcelWriter writer = ExcelUtil.getBigWriter("d:/test/dataWithNumber.xlsx");

		writer.writeRow(ListUtil.of("姓名", "编号"));
		writer.writeRow(ListUtil.of("张三", new FormulaCellValue("010001")));
		writer.writeRow(ListUtil.of("李四", new FormulaCellValue("120001")));
		writer.writeRow(ListUtil.of("王五", 123456));

		writer.close();
	}
}
