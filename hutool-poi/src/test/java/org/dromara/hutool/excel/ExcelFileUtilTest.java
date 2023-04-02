package org.dromara.hutool.excel;

import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.io.IoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ExcelFileUtilTest {

	@Test
	public void xlsTest(){
		final InputStream in = FileUtil.getInputStream("aaa.xls");
		try{
			Assertions.assertTrue(ExcelFileUtil.isXls(in));
			Assertions.assertFalse(ExcelFileUtil.isXlsx(in));
		} finally {
			IoUtil.closeQuietly(in);
		}
	}

	@Test
	public void xlsxTest(){
		final InputStream in = FileUtil.getInputStream("aaa.xlsx");
		try{
			Assertions.assertFalse(ExcelFileUtil.isXls(in));
			Assertions.assertTrue(ExcelFileUtil.isXlsx(in));
		} finally {
			IoUtil.closeQuietly(in);
		}
	}
}
