package org.dromara.hutool.poi.excel.shape;

import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.poi.excel.WorkbookUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExcelPicUtilTest {
	@Test
	@Disabled
	void readPicTest() {
		final Workbook book = WorkbookUtil.createBook("d:/test/poi/a.xlsx");
		final List<Picture> picMap = ExcelPicUtil.getShapePics(
			WorkbookUtil.createBook("d:/test/poi/a.xlsx"), 0);
		Console.log(picMap);

//		final List<? extends PictureData> allPictures = book.getAllPictures();
//		for (PictureData shape : allPictures) {
//			Console.log(shape);
//		}

		IoUtil.closeQuietly(book);
	}
}
