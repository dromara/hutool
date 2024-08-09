package cn.hutool.poi.excel;

import org.apache.poi.ss.usermodel.Workbook;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class WorkbookUtilTest {

	@Test
	public void createBookTest(){
		Workbook book = WorkbookUtil.createBook(true);
		assertNotNull(book);

		book = WorkbookUtil.createBook(false);
		assertNotNull(book);
	}
}
