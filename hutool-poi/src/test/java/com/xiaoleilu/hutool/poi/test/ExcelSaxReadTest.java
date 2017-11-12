package com.xiaoleilu.hutool.poi.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.poi.excel.ExcelUtil;
import com.xiaoleilu.hutool.poi.excel.sax.Excel03SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.Excel07SaxReader;
import com.xiaoleilu.hutool.poi.excel.sax.handler.RowHandler;

/**
 * Excel sax方式读取
 * @author looly
 *
 */
public class ExcelSaxReadTest {
	
	@Test
	public void excel07Test() {
		Excel07SaxReader reader = new Excel07SaxReader(createRowHandler());
		reader.read("aaa.xlsx", 0);
		
		//工具化快速读取
		ExcelUtil.read07BySax("aaa.xlsx", 0, createRowHandler());
	}
	
	@Test
	public void excel03Test() {
		Excel03SaxReader reader = new Excel03SaxReader(createRowHandler());
		reader.read("aaa.xls", 1);
//		Console.log("Sheet index: [{}], Sheet name: [{}]", reader.getSheetIndex(), reader.getSheetName());
		ExcelUtil.read03BySax("aaa.xls", 1, createRowHandler());
	}
	
	private RowHandler createRowHandler() {
		return new RowHandler() {

			@Override
			public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
//				Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist);
				Assert.assertTrue(CollUtil.isNotEmpty(rowlist));
			}
		};
	}
}
