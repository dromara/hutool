package org.dromara.hutool.poi.excel.writer;

import org.dromara.hutool.poi.excel.ExcelUtil;
import org.dromara.hutool.poi.excel.SimpleClientAnchor;
import org.dromara.hutool.poi.excel.style.LineStyle;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.Color;

public class WriteLineShapeTest {
	@Test
	@Disabled
	void testWriteLineShape() {
		// Setup
		final ExcelWriter writer = ExcelUtil.getWriter("d:/test/lineShape.xlsx");
		final SimpleClientAnchor clientAnchor = new SimpleClientAnchor(0, 0, 1, 1);
		final LineStyle lineStyle = LineStyle.SOLID;
		final int lineWidth = 1;

		// Execute
		writer.writeLineShape(clientAnchor, lineStyle, lineWidth, Color.RED);
		writer.close();
	}

}
