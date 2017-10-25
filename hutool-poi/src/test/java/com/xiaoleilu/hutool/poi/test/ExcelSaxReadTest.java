package com.xiaoleilu.hutool.poi.test;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.poi.excel.sax.Excel07SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.RowHandler;

public class ExcelSaxReadTest {
	
	@Test
	@Ignore
	public void excel07Test() {
		Excel07SaxReader reader = new Excel07SaxReader((new RowHandler() {

			@Override
			public void handle(int sheetIndex, int rowIndex, List<String> rowlist) {
				Console.log("{} {}", rowlist.size(), rowlist);
			}
		}));
		reader.read("d:/text.xlsx", 0);
	}
}
