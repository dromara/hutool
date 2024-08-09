package cn.hutool.poi.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.cell.CellSetter;
import cn.hutool.poi.excel.style.StyleUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 设置列样式测试
 */
public class WriteStyleTest {
	@Test
	@Disabled
	public void writeTest() {
		List<?> row1 = CollUtil.newArrayList("aaaaa", "bb", "cc", "dd", DateUtil.date(), 3.22676575765);
		List<?> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1", DateUtil.date(), 250.7676);
		List<?> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2", DateUtil.date(), 0.111);
		List<?> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3", DateUtil.date(), 35);
		List<?> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4", DateUtil.date(), 28.00);
		List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

		ExcelWriter writer = ExcelUtil.getWriter("d:/test/writeTest.xlsx");
		writer.setStyleSet(null);

		final CellStyle cellStyle = StyleUtil.createDefaultCellStyle(writer.getWorkbook());
		cellStyle.setFont(StyleUtil.createFont(writer.getWorkbook(), Font.COLOR_RED, (short) 12, "宋体"));

		writer.write(rows);

		writer.writeCellValue(0, 0, (CellSetter) cell -> {
			cell.setCellStyle(cellStyle);
			cell.setCellValue("1234");
		});

		for (int i = 0; i < writer.getRowCount(); i++) {
			writer.setStyle(cellStyle, 2, i);
		}

		writer.close();
	}
}
