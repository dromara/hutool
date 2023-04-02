package org.dromara.hutool.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WorkbookUtilTest {

	@Test
	public void createBookTest(){
		Workbook book = WorkbookUtil.createBook(true);
		Assertions.assertNotNull(book);

		book = WorkbookUtil.createBook(false);
		Assertions.assertNotNull(book);
	}
}
