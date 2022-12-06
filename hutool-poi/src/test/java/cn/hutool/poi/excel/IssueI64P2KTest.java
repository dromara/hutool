package cn.hutool.poi.excel;

import cn.hutool.core.collection.ListUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Ignore;
import org.junit.Test;

public class IssueI64P2KTest {

	@Test
	@Ignore
	public void writeWithColumnStyleTest() {
		// 设置默认列样式无效，暂时无解。
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/columnColorTest.xlsx");
		writer.disableDefaultStyle();

		final Font font = writer.createFont();
		font.setColor(Font.COLOR_RED);
		final CellStyle style = writer.createColumnStyle(0);
		style.setFont(font);
		style.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());

		writer.writeRow(ListUtil.toList("aaa"));
		writer.writeRow(ListUtil.toList("aaa"));

		writer.close();
	}
}
