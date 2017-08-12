package com.xiaoleilu.hutool.poi.test;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.poi.excel.ExcelReader;

public class PoiTest {
	
	@Test
	@Ignore
	public void excelReadTest() {
		ExcelReader reader = new ExcelReader(FileUtil.getInputStream("d:/dwdm.xls"), 0);
		reader.setTrimCellValue(true);
		List<List<Object>> readAll = reader.read();
		for (List<Object> list : readAll) {
			Console.log(list.get(4));
		}
	}
}
