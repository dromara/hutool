package cn.hutool.poi.excel;

import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class IssueI5Q1TWTest {

	@Test
	@Ignore
	public void readTest(){
		final ExcelReader reader = ExcelUtil.getReader("d:/test/I5Q1TW.xlsx");
		final List<List<Object>> read = reader.read();
		Console.log(reader.readCellValue(0, 0));
	}
}
