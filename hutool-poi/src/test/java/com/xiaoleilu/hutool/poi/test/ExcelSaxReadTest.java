package com.xiaoleilu.hutool.poi.test;

import java.util.List;

import org.junit.Test;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.poi.excel.sax.Excel03SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.Excel07SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.RowHandler;

/**
 * Excel sax方式读取
 * @author looly
 *
 */
public class ExcelSaxReadTest {
	
	@Test
	public void excel07Test() {
		Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
		reader.read("d:/text.xlsx", 0);
	}
	
	@Test
	public void excel03Test() {
		Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
		reader.read("d:/text.xls", 1);
		
		Console.log(reader.getSheetIndex());
		Console.log(reader.getSheetName());
	}
	
	private RowHandler createRowHandler() {
		return new RowHandler() {

			@Override
			public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
				Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist);
			}
		};
	}
}
