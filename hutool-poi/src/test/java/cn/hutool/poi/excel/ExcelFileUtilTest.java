package cn.hutool.poi.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ExcelFileUtilTest {

	@Test
	public void xlsTest(){
		InputStream in = FileUtil.getInputStream("aaa.xls");
		try{
			Assertions.assertTrue(ExcelFileUtil.isXls(in));
			Assertions.assertFalse(ExcelFileUtil.isXlsx(in));
		} finally {
			IoUtil.close(in);
		}
	}

	@Test
	public void xlsxTest(){
		InputStream in = FileUtil.getInputStream("aaa.xlsx");
		try{
			Assertions.assertFalse(ExcelFileUtil.isXls(in));
			Assertions.assertTrue(ExcelFileUtil.isXlsx(in));
		} finally {
			IoUtil.close(in);
		}
	}
}
