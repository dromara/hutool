package cn.hutool.poi.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Test;

public class WorkbookUtilTest {

	@Test
	public void createBookTest(){
		Workbook book = WorkbookUtil.createBook(true);
		Assert.assertNotNull(book);

		book = WorkbookUtil.createBook(false);
		Assert.assertNotNull(book);
	}
}
