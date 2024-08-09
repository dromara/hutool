package cn.hutool.poi.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ExcelFileUtilTest {

	@Test
	public void xlsTest(){
		InputStream in = FileUtil.getInputStream("aaa.xls");
		try{
			assertTrue(ExcelFileUtil.isXls(in));
			assertFalse(ExcelFileUtil.isXlsx(in));
		} finally {
			IoUtil.close(in);
		}
	}

	@Test
	public void xlsxTest(){
		InputStream in = FileUtil.getInputStream("aaa.xlsx");
		try{
			assertFalse(ExcelFileUtil.isXls(in));
			assertTrue(ExcelFileUtil.isXlsx(in));
		} finally {
			IoUtil.close(in);
		}
	}
}
