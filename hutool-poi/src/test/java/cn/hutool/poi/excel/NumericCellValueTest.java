package cn.hutool.poi.excel;

import cn.hutool.poi.excel.cell.values.NumericCellValue;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

public class NumericCellValueTest {

	@Test
	public void writeTest() {
		final ExcelReader reader = ExcelUtil.getReader("1899bug_demo.xlsx");
		ExcelWriter writer = ExcelUtil.getWriter("1899bug_write.xlsx");
		Cell cell = reader.getCell(0, 0);
		// 直接取值
		// 和CellUtil.getCellValue(org.apache.poi.ss.usermodel.Cell)方法的结果一样
		// 1899-12-31 04:39:00
		Date cellValue = cell.getDateCellValue();
		// 将这个值写入EXCEL中自定义样式的单元格，结果会是-1
		writer.writeCellValue(0, 0, cellValue);
		// 修改后的写入，单元格内容正常
		writer.writeCellValue(1, 0, new NumericCellValue(cell).getValue());
		writer.close();
		reader.close();
	}
}
