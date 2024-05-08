package cn.hutool.poi.excel;

import cn.hutool.core.collection.ListUtil;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.Ignore;
import org.junit.Test;

public class WriteNumberToStringTest {
	@Test
	@Ignore
	public void writeNumberTest() {
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/dataWithNumber.xlsx");
		final XSSFSheet sheet = (XSSFSheet) writer.getSheet();
		sheet.addIgnoredErrors(new CellRangeAddress(0, 100, 0, 100), IgnoredErrorType.NUMBER_STORED_AS_TEXT);
		final CellStyle cellStyle = writer.getStyleSet().getCellStyle();
		cellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("TEXT"));

		writer.writeRow(ListUtil.of("姓名", "编号"));
		writer.writeRow(ListUtil.of("张三", "010001"));
		writer.writeRow(ListUtil.of("李四", "120001"));
		writer.writeRow(ListUtil.of("王五", 123456));

		writer.close();
	}
}
