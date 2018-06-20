package cn.hutool.poi.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;

/**
 * Excel sax方式读取
 * @author looly
 *
 */
public class ExcelSaxReadTest {
	
	@Test
	@Ignore
	public void readBlankLineTest() {
		ExcelUtil.readBySax("e:/ExcelBlankLine.xlsx", 0, new RowHandler() {
			
			@Override
			public void handle(int sheetIndex, int rowIndex, List<Object> rowList) {
				if(StrUtil.isAllEmpty(Convert.toStrArray(rowList))) {
					return;
				}
				Console.log(rowList);
			}
		});
	}
	
	@Test
	public void readBySaxTest() {
		ExcelUtil.readBySax("blankAndDateTest.xlsx", 0, createRowHandler());
	}
	
	@Test
	@Ignore
	public void readBySaxTest2() {
		ExcelUtil.readBySax("e:/B23_20180404164901240.xlsx", 2, new RowHandler() {
			@Override
			public void handle(int sheetIndex, int rowIndex, List<Object> rowList) {
				Console.log(rowList);
			}
		});
	}
	
	@Test
	@Ignore
	public void readBySaxTest3() {
		ExcelUtil.readBySax("e:/test.xlsx", 0, new RowHandler() {
			
			@Override
			public void handle(int sheetIndex, int rowIndex, List<Object> rowList) {
				Console.log(rowList);
			}
		});
	}
	
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
				if(5 != rowIndex && 6 != rowIndex) {
					//测试样例中除第五行、第六行都为非空行
					Assert.assertTrue(CollUtil.isNotEmpty(rowlist));
				}
			}
		};
	}
}
